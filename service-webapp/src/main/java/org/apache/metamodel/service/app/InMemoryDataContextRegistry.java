/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.metamodel.service.app;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.metamodel.DataContext;
import org.apache.metamodel.pojo.PojoDataContext;

public class InMemoryDataContextRegistry implements DataContextRegistry {

    private final Map<String, Supplier<DataContext>> dataContextIdentifiers;

    public InMemoryDataContextRegistry() {
        dataContextIdentifiers = new LinkedHashMap<>();
    }

    @Override
    public String registerDataContext(final String dataContextIdentifier, final DataContextDefinition dataContextDef)
            throws IllegalArgumentException {
        if (dataContextIdentifiers.containsKey(dataContextIdentifier)) {
            throw new IllegalArgumentException("DataContext already exist: " + dataContextIdentifier);
        }
        dataContextIdentifiers.put(dataContextIdentifier, new Supplier<DataContext>() {
            @Override
            public DataContext get() {
                // TODO: Do a proper transformation from definition to instance
                return new PojoDataContext();
            }
        });
        return dataContextIdentifier;
    }

    @Override
    public List<String> getDataContextIdentifiers() {
        return dataContextIdentifiers.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public DataContext openDataContext(String dataContextIdentifier) {
        final Supplier<DataContext> supplier = dataContextIdentifiers.get(dataContextIdentifier);
        if (supplier == null) {
            throw new IllegalArgumentException("No such DataContext: " + dataContextIdentifier);
        }
        return supplier.get();
    }

}
