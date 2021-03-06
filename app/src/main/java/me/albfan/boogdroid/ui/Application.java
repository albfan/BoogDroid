/*
 *  BugsControl
 *  Copyright (C) 2014  Jon Ander Peñalba
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.albfan.boogdroid.ui;


import android.content.Context;

import com.activeandroid.query.Select;

import java.util.List;

import me.albfan.boogdroid.general.Server;

public class Application extends com.activeandroid.app.Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            final List<me.albfan.boogdroid.db.Server> dbServers = new Select().from(me.albfan.boogdroid.db.Server.class).execute();
            Server.servers.clear();
            for (final me.albfan.boogdroid.db.Server s : dbServers) {
                Server.servers.add(new me.albfan.boogdroid.bugzilla.Server(s));
            }
        } catch (Exception e) {
        }
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
