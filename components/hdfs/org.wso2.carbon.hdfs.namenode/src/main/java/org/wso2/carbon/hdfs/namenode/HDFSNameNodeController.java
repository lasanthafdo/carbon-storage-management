/*
 *  Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.hdfs.namenode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="org.wso2.carbon.hdfs.namenode.component" immediate="true"
 * @scr.reference name="user.realmservice.default" interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService"  unbind="unsetRealmService"
 */

public class HDFSNameNodeController {

    private static Log log = LogFactory.getLog(HDFSNameNodeController.class);
    private RealmService realmService;
    private String FALSE = "false";
    protected void activate(ComponentContext componentContext) throws Throwable {
    	String enableHDFSstartup = System.getProperty("enable.hdfs.startup");
    	
        if (log.isDebugEnabled()) {
            log.debug("HDFS Name Node bunddle is activated.");
        }
        if ((FALSE.equals(enableHDFSstartup))) {
            log.debug("HDFS name node is disabled and not started in the service activator");
            return;
        }
        HDFSNameNodeComponentManager.getInstance().init(realmService);
        HDFSNameNode nameNode = new HDFSNameNode();
        nameNode.start(); // start name node
    }

    protected void deactivate(ComponentContext componentContext) {
        if (log.isDebugEnabled()) {
            log.debug("HDFS Name Node bunddle is deactivated.");
        }
        HDFSNameNodeComponentManager.getInstance().destroy();
    }

    protected void setRealmService(RealmService realmService) {
        this.realmService = realmService;
    }

    protected void unsetRealmService(RealmService realmService) {
        this.realmService = null;
    }
}

