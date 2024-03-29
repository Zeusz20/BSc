package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.dialog.LoadingDialog;
import com.zeusz.bsc.app.ui.Game;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.app.widget.SendButton;
import com.zeusz.bsc.core.Cloud;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Project;

import java.net.URLEncoder;
import java.util.regex.Pattern;


public class GameClient extends Channel {

    /**
     * @param state
     *  In which state the client will be after creation.
     * @param action
     *  Which action will the client perform (host or join).
     * @param initial
     *  The initial message to the server. This holds the name of the projects
     *  in case of hosting a game; and the ID of the game in case of joining to one.
     * */
    private static void launch(Activity ctx, Project project, State state, String action, String initial) {
        new Thread(new Task(ctx, () -> {
            GameClient client = new GameClient(ctx, project);

            ((MainActivity) ctx).setGameClient(client);
            client.setState(state);
            client.connect();   // connect to server
            client.listen();    // wait for response from server
            client.handshake(action, initial);   // send intent (create/join game) and initial message (project name/game id)
        })).start();
    }

    public static void createGame(Activity ctx, Project project) {
        if(!project.valid)
            ViewManager.toast(ctx, Localization.localize("game.invalid_project"));

        else if(Channel.isAvailable(ctx))
            launch(ctx, project, State.CREATE, SERVER_INFO.getString("create"), project.getSource().getName());
    }

    public static void joinGame(Activity ctx, String id) {
        if(!Channel.isAvailable(ctx) || !Pattern.matches(SERVER_INFO.getString("id_pattern"), id))
            return;

        GameClient client = ((MainActivity) ctx).getGameClient();

        if(client == null) {
            launch(ctx, null, State.JOIN, SERVER_INFO.getString("join"), id);
        }
        else {
            // client is already running
            // this is needed when the player enters a wrong game id, so the client is not destroyed and can be reused
            new Thread(new Task(ctx, () -> client.handshake(SERVER_INFO.getString("join"), id))).start();
        }
    }

    /* Client states */
    public enum State { CREATE, WAITING, JOIN, FILE_DOWNLOAD, IN_GAME, EXIT }

    /* Class fields and methods */
    protected Game game;
    protected boolean opponentReady;

    protected String id;
    protected State state;
    protected boolean isHost;

    public GameClient(Activity ctx, Project project) throws Exception {
        super(ctx);
        game = new Game(ctx, project);
        IOManager.resetFileWriter();
    }

    public void setMeta(boolean isHost, String id) {
        this.isHost = isHost;
        this.id = id.toUpperCase();
    }

    public void setState(State state) { this.state = state; }

    public void setId(String id) { this.id = id; }

    public String getId() { return id; }

    public Game getGame() { return game; }

    public boolean isDirty() { return state != State.EXIT; }

    public void selectObject(Object object) {
        game.setObject(object);

        try {
            // player has chosen an object, inform opponent about it
            send(SERVER_INFO.getString("ready"));
        }
        catch(Exception e) {
            ctx.setGameClient(null);
        }

        // wait for other player to choose an object
        ViewManager.show(ctx, opponentReady ? ViewManager.GAME_SCREEN : ViewManager.LOADING_SCREEN);
    }

    @Override
    public void parse(String response) throws Exception {
        synchronized(Channel.INPUT_LOCK) {
            if(response == null) return;

            if(isDirty() && SERVER_INFO.getString("disconnect").equals(response)) {
                ctx.setGameClient(null);
                game.exit(ctx, isDirty());

                if(state == State.FILE_DOWNLOAD)
                    IOManager.getFileWriter().interrupt();
            }
            else {
                switch(state) {
                    case CREATE:
                    case JOIN:
                        // the activity which creates the game is the host
                        init((state == State.CREATE), response);
                        break;
                    case WAITING:
                        wait(response);
                        break;
                    case FILE_DOWNLOAD:
                        download(response);
                        break;
                    case IN_GAME:
                        update(response);
                        break;
                }
            }
        }
    }

    /**
     * Initializes the game and the client when it is in the state of CREATE or JOIN.
     * After initialization the host is put to the WAITING state, while the
     * joining player is put in the FILE_DOWNLOAD state.
     * */
    protected void init(boolean isHost, String id) {
        if(id.equals(SERVER_INFO.getString("invalid"))) {
            ViewManager.toast(ctx, Localization.localize("game.invalid"));
            return;
        }

        setMeta(isHost, id);
        setState(isHost ? State.WAITING : State.FILE_DOWNLOAD);
        SendButton.init(isHost); // who will start the first turn

        // render waiting screen
        if(isHost)
            ViewManager.show(ctx, ViewManager.LOADING_SCREEN);
    }

    /**
     * While client is in the WAITING state, it waits for the other player to
     * download the project file (if not present on their machine).
     * The connection is established when the server sends the "ready" response.
     * */
    protected void wait(String response) throws Exception {
        if(SERVER_INFO.getString("download").equals(response)) {
            // send project to joining player
            byte[] file = IOManager.getFileBytes(game.getProject().getSource());
            send(SERVER_INFO.getString("download"));  // signal server the beginning of file transfer
            send(IOManager.getFileWriter().bytesToString(file));
            send(SERVER_INFO.getString("over"));  // signal end of file transfer
        }
        else if(SERVER_INFO.getString("ready").equals(response)) {
            // start game
            load(null);
        }
    }

    /**
     * In the FILE_DOWNLOAD state the joining player downloads the project
     * file to their machine if not present. After the download finished the
     * {@link #load(String)} method will load the project and the client's
     * state will be changed to IN_GAME.
     * */
    protected void download(String response) throws Exception {
        if(!isHost) {
            if(response.equals(SERVER_INFO.getString("over"))) {
                // file bytes received
                LoadingDialog.hide(ctx);
                IOManager.getFileWriter().close();
                send(SERVER_INFO.getString("over"));
                load(IOManager.getFileWriter().getFile().getName());
            }
            else if(!response.endsWith(".gwp")) {  // if response is not a filename
                // save file bytes from host
                IOManager.getFileWriter().write(response);
            }
            else {
                // download project from server
                String encoded = URLEncoder.encode(response, SERVER_INFO.getString("encoding"));
                String url = Cloud.getCloudUrl("/projects/" + encoded);
                IOManager.download(ctx, url);  // download project file if missing
            }
        }
    }

    /**
     * Loads the project to memory.
     * If joining player doesn't have the project file, this method is invoked
     * by the {@link DownloadReceiver} class when the project is finished downloading.
     *
     * If the download fails, meaning the host player is using a manually
     * added project, which is not present on the server, request the project
     * to be transferred and handle it in the FILE_DOWNLOAD state.
     * */
    public void load(String filename) throws Exception {
        // host already has the project loaded
        if(!isHost) {
            game.loadProject(ctx, filename);

            if(game.getProject() == null) {  // download failed, other player is using a manually added project
                if(IOManager.getFileWriter().isOpen()) {  // request file transfer ONCE, if it fails the first time disconnect
                    LoadingDialog.display(ctx);
                    IOManager.getFileWriter().init(ctx, filename);
                    setState(State.FILE_DOWNLOAD);
                    send(SERVER_INFO.getString("download"));
                }
                else {
                    LoadingDialog.hide(ctx);
                    ctx.setGameClient(null);
                }
            }
            else {
                send(SERVER_INFO.getString("ready"));
            }
        }

        if(game.getProject() != null) {
            setState(State.IN_GAME);
            ViewManager.show(ctx, ViewManager.OBJECT_SELECTION);
        }
    }

    /**
     * While in the IN_GAME state wait for other player to choose their object.
     * If the object is already chosen, handle JSON response from the server.
     * */
    protected void update(String response) {
        if(SERVER_INFO.getString("ready").equals(response)) {
            // dismiss loading screen because opponent has chosen an object
            // (at the beginning of the game)
            opponentReady = true;

            // player has already chosen an object
            if(game.getObject() != null)
                ViewManager.show(ctx, ViewManager.GAME_SCREEN);
        }
        else {
            // update game state
            game.update(ctx, new Dictionary(response));
        }
    }

    public void sendJSON(Dictionary message) {
        try { send(message.toString()); }
        catch(Exception e) { ctx.setGameClient(null); }
    }

}
