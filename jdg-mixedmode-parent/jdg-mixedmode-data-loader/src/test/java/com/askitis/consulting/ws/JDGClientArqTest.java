/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.askitis.consulting.ws;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.infinispan.manager.CacheContainer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.askitis.consulting.jdg.manager.CacheManagerProvider;
import com.askitis.consulting.wshelloworld.HelloWorldService;


/**
 * tests for the RdsJDGDataLoad Web Service to demonstrate accessing the web service using a client

 * 
 * @author stkousso@redhat.com
 */
@RunWith(Arquillian.class)
public class JDGClientArqTest {
    /**
     * The location of the WebApp source folder so we know where to find the web.xml when deploying using Arquillian.
     */
    private static final String WEBAPP_SRC = "src/main/webapp";
    /**
     * The name of the WAR Archive that will be used by Arquillian to deploy the application.
     */
    private static final String APP_NAME = "rds-loader-ws";
    /**
     * The path of the WSDL endpoint in relation to the deployed web application.
     */
    private static final String WSDL_PATH = "RdsJDGDataLoadService?wsdl";

    @ArquillianResource
    private URL deploymentUrl;

    private RdsJDGDataLoadService client;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, APP_NAME + ".war").addPackage(RdsJDGDataLoadService.class.getPackage())
                .addAsWebInfResource(new File(WEBAPP_SRC, "WEB-INF/web.xml"));
    }

    @Before
    public void setup() {
        try {
            client = new JDGLoadClient(new URL(deploymentUrl, WSDL_PATH));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHello() {
        System.out.println("[Client] Requesting the WebService to load JDG Data.");

        // Get a response from the WebService
        final Integer response = client.loadJDGData(Mode.EMBEDDED, CacheManagerProvider.CACHE_NAMES.RDS_SYNC_DIST.name());
        assertNotNull(response);
        assertEquals(response, new Integer(0));

        System.out.println("[WebService] " + response);
    }
}
