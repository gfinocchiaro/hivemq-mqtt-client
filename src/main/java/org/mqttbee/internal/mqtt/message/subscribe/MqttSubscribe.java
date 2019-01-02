/*
 * Copyright 2018 The MQTT Bee project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.mqttbee.internal.mqtt.message.subscribe;

import org.jetbrains.annotations.NotNull;
import org.mqttbee.annotations.Immutable;
import org.mqttbee.internal.mqtt.datatypes.MqttUserPropertiesImpl;
import org.mqttbee.internal.mqtt.message.MqttMessageWithUserProperties;
import org.mqttbee.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import org.mqttbee.util.collections.ImmutableList;

/**
 * @author Silvio Giebl
 */
@Immutable
public class MqttSubscribe extends MqttMessageWithUserProperties implements Mqtt5Subscribe {

    private final @NotNull ImmutableList<MqttSubscription> subscriptions;

    public MqttSubscribe(
            final @NotNull ImmutableList<MqttSubscription> subscriptions,
            final @NotNull MqttUserPropertiesImpl userProperties) {

        super(userProperties);
        this.subscriptions = subscriptions;
    }

    @Override
    public @NotNull ImmutableList<MqttSubscription> getSubscriptions() {
        return subscriptions;
    }

    public @NotNull MqttStatefulSubscribe createStateful(final int packetIdentifier, final int subscriptionIdentifier) {
        return new MqttStatefulSubscribe(this, packetIdentifier, subscriptionIdentifier);
    }

    @Override
    public @NotNull MqttSubscribeBuilder.Default extend() {
        return new MqttSubscribeBuilder.Default(this);
    }
}