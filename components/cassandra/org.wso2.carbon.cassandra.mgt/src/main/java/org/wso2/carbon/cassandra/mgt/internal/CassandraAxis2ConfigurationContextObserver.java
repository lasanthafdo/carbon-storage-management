/*
 *  Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.cassandra.mgt.internal;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.cassandra.mgt.authorize.CassandraAuthorizationUtils;
import org.wso2.carbon.cassandra.mgt.authorize.CassandraAuthorizer;
import org.wso2.carbon.user.api.AuthorizationManager;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.utils.AbstractAxis2ConfigurationContextObserver;

/**
 * To get the events when a new Tenant AxisConfig is terminated
 * Remove all the hector cluster instances created by the terminated client
 */
public class CassandraAxis2ConfigurationContextObserver extends AbstractAxis2ConfigurationContextObserver {

	private static final Log log = LogFactory.getLog(CassandraAxis2ConfigurationContextObserver.class);

	public void createdConfigurationContext(ConfigurationContext configurationContext) {
		try {
			if(!CassandraAuthorizer.isServiceProviderExist()) {
				CassandraAuthorizer.createServiceProvider();
				CassandraAuthorizer.definePermissionsForTenant();
				//TODO remove when application management feature handle this
				UserRealm userRealm = CassandraAdminDataHolder.getInstance().getRealmForCurrentTenant();
				AuthorizationManager authorizationManager = userRealm.getAuthorizationManager();
				authorizationManager.authorizeRole(userRealm.getRealmConfiguration().getAdminRoleName(),
						CassandraAuthorizationUtils.getApplicationResourcePath(), CassandraAuthorizationUtils.UI_EXECUTE);
			}
		} catch (Exception e) {
			log.error("Setting Cassandra permissions for tenant admin role failed.", e);
		}
	}

	public void terminatingConfigurationContext(ConfigurationContext configurationContext) {
	}

}
