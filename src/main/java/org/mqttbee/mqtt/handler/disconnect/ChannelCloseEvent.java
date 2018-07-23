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

package org.mqttbee.mqtt.handler.disconnect;

import io.reactivex.CompletableEmitter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mqttbee.api.mqtt.mqtt5.exceptions.Mqtt5MessageException;
import org.mqttbee.api.mqtt.mqtt5.message.Mqtt5Message;
import org.mqttbee.mqtt.message.disconnect.MqttDisconnect;

/**
 * Event that is fired when the channel will be closed containing the cause.
 * <p>
 * Only one such event is fired in all cases:
 * <ul>
 * <li>Server sent a DISCONNECT message</li>
 * <li>Client sends a DISCONNECT message</li>
 * <li>Server closed the channel without a DISCONNECT message</li>
 * <li>Client closes the channel without a DISCONNECT message</li>
 * </ul>
 *
 * @author Silvio Giebl
 */
public class ChannelCloseEvent {

    private final Throwable cause;
    private final boolean fromClient;
    private final CompletableEmitter completableEmitter;

    ChannelCloseEvent(
            @NotNull final Throwable cause, final boolean fromClient,
            @Nullable final CompletableEmitter completableEmitter) {

        this.cause = cause;
        this.fromClient = fromClient;
        this.completableEmitter = completableEmitter;
    }

    /**
     * @return the cause for closing of the channel.
     */
    @NotNull
    public Throwable getCause() {
        return cause;
    }

    /**
     * @return whether the client initiated closing of the channel.
     */
    boolean fromClient() {
        return fromClient;
    }

    /**
     * @return the DISCONNECT message which was sent or received, otherwise null.
     */
    @Nullable
    MqttDisconnect getDisconnect() {
        if (cause instanceof Mqtt5MessageException) {
            final Mqtt5Message mqttMessage = ((Mqtt5MessageException) cause).getMqttMessage();
            if (mqttMessage instanceof MqttDisconnect) {
                return (MqttDisconnect) mqttMessage;
            }
        }
        return null;
    }

    /**
     * @return the emitter to indicate success or error (only possible if {@link #fromClient()} = true, {@link
     *         #getDisconnect()} != null).
     */
    @Nullable
    CompletableEmitter getCompletableEmitter() {
        return completableEmitter;
    }

}
