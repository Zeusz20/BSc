# Generated by Django 4.0.4 on 2022-08-14 07:51

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('main', '0003_auto_20220814_0950'),
    ]

    operations = [
        migrations.AddField(
            model_name='project',
            name='deleted',
            field=models.BooleanField(default=False),
        ),
        migrations.AlterField(
            model_name='project',
            name='date_added',
            field=models.TimeField(default=datetime.datetime(2022, 8, 14, 9, 51, 32, 872446)),
        ),
    ]
