/*
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
package org.apache.rya.streams.kafka.serialization;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * A Kafka {@link Serializer} that is able to serialize entities using Java
 * object serialization.
 *
 * @param T - The type of entity to serialize.
 */
@DefaultAnnotation(NonNull.class)
public abstract class ObjectSerializer<T> implements Serializer<T> {

    private static final Logger log = LoggerFactory.getLogger(ObjectSerializer.class);

    @Override
    public void configure(final Map<String, ?> configs, final boolean isKey) {
        // Nothing to do.
    }

    @Override
    public byte[] serialize(final String topic, final T data) {
        if(data == null) {
            return null;
        }

        try {
            return ObjectSerialization.serialize(data);
        } catch (final IOException e) {
            log.error("Unable to serialize a " + getSerializedClass().getName() + ".", e);

            // Return null when there is an error since that is the contract of this method.
            return null;
        }
    }


    @Override
    public void close() {
        // Nothing to do.
    }

    /**
     * @return - The class name of T. This is used for logging purposes.
     */
    protected abstract Class<T> getSerializedClass();
}