/*
 * Copyright 2018 dc-square and the HiveMQ MQTT Client Project
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

package com.hivemq.client.mqtt.mqtt3.message.connect.connack;

import com.hivemq.client.annotations.DoNotImplement;
import com.hivemq.client.mqtt.mqtt3.message.Mqtt3Message;
import com.hivemq.client.mqtt.mqtt3.message.Mqtt3MessageType;
import org.jetbrains.annotations.NotNull;

/**
 * MQTT 3 ConnAck message. This message is translated from and to a MQTT 3 CONNACK packet.
 *
 * @author Daniel Krüger
 * @author Silvio Giebl
 * @since 1.0
 */
@DoNotImplement
public interface Mqtt3ConnAck extends Mqtt3Message {

    /**
     * @return the Return Code of this ConnAck message.
     */
    @NotNull Mqtt3ConnAckReturnCode getReturnCode();

    /**
     * @return whether the server has a session present.
     */
    boolean isSessionPresent();

    @Override
    default @NotNull Mqtt3MessageType getType() {
        return Mqtt3MessageType.CONNACK;
    }
}