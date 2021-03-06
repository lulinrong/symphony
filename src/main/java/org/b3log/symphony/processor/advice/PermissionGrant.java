/*
 * Symphony - A modern community (forum/SNS/blog) platform written in Java.
 * Copyright (C) 2012-2016,  b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.b3log.symphony.processor.advice;

import org.b3log.latke.Keys;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.User;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.advice.AfterRequestProcessAdvice;
import org.b3log.latke.servlet.renderer.AbstractHTTPResponseRenderer;
import org.b3log.symphony.model.Permission;
import org.b3log.symphony.model.Role;
import org.b3log.symphony.service.RoleQueryService;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Permission grant.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Dec 12, 2016
 * @since 1.8.0
 */
@Named
@Singleton
public class PermissionGrant extends AfterRequestProcessAdvice {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PermissionGrant.class);

    /**
     * Role query service.
     */
    @Inject
    private RoleQueryService roleQueryService;

    @Override
    public void doAdvice(final HTTPRequestContext context, final Object ret) {
        final HttpServletRequest request = context.getRequest();

        Map<String, JSONObject> permissionsGrant;
        final JSONObject user = (JSONObject) request.getAttribute(User.USER);
        if (null != user) {
            permissionsGrant = roleQueryService.getUserPermissionsGrantMap(user.optString(Keys.OBJECT_ID));
        } else {
            permissionsGrant = roleQueryService.getPermissionsGrantMap(Role.ROLE_ID_C_VISITOR);
        }

        final AbstractHTTPResponseRenderer renderer = context.getRenderer();
        if (null != renderer) {
            final Map<String, Object> dataModel = renderer.getRenderDataModel();

            dataModel.put(Permission.PERMISSIONS, permissionsGrant);
        }
    }
}
