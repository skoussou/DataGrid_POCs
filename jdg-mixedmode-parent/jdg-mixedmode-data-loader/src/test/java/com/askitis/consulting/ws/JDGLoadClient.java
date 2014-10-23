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

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.askitis.consulting.rds.data.DerivativesDTO;
import com.askitis.consulting.rds.model.DerivativeProduct;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * A Client stub to the RdsJDGDataLoad JAX-WS Web Service.
 * 
 * @author stkousso@redhat.com
 */
public class JDGLoadClient implements RdsJDGDataLoadService {
    private RdsJDGDataLoadService jdgLoadService;

    /**
     * Default constructor
     * 
     * @param url The URL to the RdsJDGDataLoad WSDL endpoint.
     */
    public JDGLoadClient(final URL wsdlUrl) {
        QName serviceName = new QName("http://www.askitis.com/jdg/load", "HelloWorlRdsJDGDataLoadServicedService");

        Service service = Service.create(wsdlUrl, serviceName);
        jdgLoadService = service.getPort(RdsJDGDataLoadService.class);
        assert (jdgLoadService != null);
    }
    
    /**
     * Default constructor
     * 
     * @param url The URL to the RdsJDGDataLoad WSDL endpoint.
     * @throws MalformedURLException if the WSDL url is malformed.
     */
    public JDGLoadClient(final String url) throws MalformedURLException {
        this(new URL(url));
    }

    /**
     * Gets the Web Service to load JDG content
     */
    @Override
	public Integer loadJDGData(Mode mode, String CacheName) {
		return jdgLoadService.loadJDGData(Mode.EMBEDDED, CacheName);
	}
    

	@Override
	public DerivativesDTO retrieveDGData(Mode mode, String cacheName, DataLocation dataFrom) {
		return jdgLoadService.retrieveDGData(Mode.EMBEDDED, cacheName, dataFrom);
	}

	@Override
	public List<DerivativeProduct> searchAllJDGData(Mode mode, String cacheName, DataLocation dataFrom) {
		return jdgLoadService.searchAllJDGData(Mode.EMBEDDED, cacheName, dataFrom);
	}

	@Override
	public List<DerivativeProduct> getAllJDGDataByMapReduceKeys(Mode mode, String cacheName) {
		return jdgLoadService.getAllJDGDataByMapReduceKeys(Mode.EMBEDDED, cacheName);
	}

	@Override
	public Boolean startCache(Mode mode, String cacheName) {
		return jdgLoadService.startCache(Mode.EMBEDDED, cacheName);
	}

}
