/**
 * DefaultLoginHandler.java 03/02/2010
 *
 * Copyright 2010 INDITEX.
 * Departamento de Sistemas
 * Area de distribucion - http://axincloud.central.inditex.grp/sky
 */
package com.bzsoft.wjdbc.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.bzsoft.wjdbc.WJdbcException;
import com.bzsoft.wjdbc.server.LoginHandler;
import com.bzsoft.wjdbc.server.config.UserConfiguration.Database;

public class DefaultLoginHandler implements LoginHandler {

    private static final String CONFIG_USERS_FILE_URL_SYSTEM_PROPERTY = "vjdbc.config.users.file";

    private final Map<String, UserConfiguration> userMap;

    public DefaultLoginHandler() throws IOException, SAXException, WJdbcException {
        final String configFileURL = System.getProperty(CONFIG_USERS_FILE_URL_SYSTEM_PROPERTY);
        if (configFileURL == null) {
            throw new WJdbcException("System property " + CONFIG_USERS_FILE_URL_SYSTEM_PROPERTY + " not found");
        }
        userMap = parse(new URL(configFileURL).openStream());
    }

    @Override
    public void checkLogin(final String databaseId, final String user, final char[] password) throws WJdbcException {
        if (user != null) {
            if (databaseId == null) {
                throw new WJdbcException("Unknown database ID: " + databaseId);
            }
            final UserConfiguration userBean = userMap.get(user);
            if (userBean == null) {
                throw new WJdbcException("Unknown user " + user);
            }
            final Database db = new Database(databaseId);
            if (!userBean.getDatabases().contains(db)) {
                throw new WJdbcException("Unable to connect to " + databaseId);
            }
            if (userBean.getPassword() != null && !userBean.getPassword().equals(password)) {
                throw new WJdbcException("Login or password is wrong");
            }
        } else {
            throw new WJdbcException("User is null");
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, UserConfiguration> parse(final InputStream is) throws IOException, SAXException {
        final Digester digester = new Digester();
        digester.push(new ArrayList<UserConfiguration>());
        digester.addObjectCreate("users/user", UserConfiguration.class);
        digester.addSetProperties("users/user");
        digester.addObjectCreate("users/user/database", Database.class);
        digester.addSetProperties("users/user/database");
        digester.addSetNext("users/user/database", "addDatabase", Database.class.getName());
        digester.addSetNext("users/user", "add", UserConfiguration.class.getName());
        final List<UserConfiguration> list = (List<UserConfiguration>) digester.parse(is);
        final Map<String, UserConfiguration> map = new HashMap<String, UserConfiguration>();
        for (final UserConfiguration userc : list) {
            map.put(userc.getLogin(), userc);
        }
        return map;
    }
}
