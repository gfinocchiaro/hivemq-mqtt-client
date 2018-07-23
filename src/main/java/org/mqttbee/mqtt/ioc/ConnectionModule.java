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

package org.mqttbee.mqtt.ioc;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import org.mqttbee.mqtt.MqttClientData;
import org.mqttbee.mqtt.handler.MqttChannelInitializer;
import org.mqttbee.mqtt.handler.auth.MqttAuthHandler;
import org.mqttbee.mqtt.handler.auth.MqttDisconnectOnAuthHandler;
import org.mqttbee.mqtt.handler.disconnect.Mqtt3DisconnectHandler;
import org.mqttbee.mqtt.handler.disconnect.Mqtt5DisconnectHandler;
import org.mqttbee.mqtt.handler.disconnect.MqttDisconnectHandler;
import org.mqttbee.mqtt.message.connect.MqttConnect;
import org.mqttbee.mqtt.netty.NettyBootstrap;

import javax.inject.Named;

/**
 * @author Silvio Giebl
 */
@Module
abstract class ConnectionModule {

    @Provides
    static Bootstrap provideBootstrap(
            final MqttClientData clientData, final NettyBootstrap nettyBootstrap,
            final MqttChannelInitializer channelInitializer) {

        return new Bootstrap().group(nettyBootstrap.getEventLoopGroup(clientData.getExecutorConfig()))
                .channel(nettyBootstrap.getChannelClass())
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(channelInitializer);
    }

    @Provides
    @ConnectionScope
    static MqttDisconnectHandler provideDisconnectHandler(
            final MqttClientData clientData, final Lazy<Mqtt5DisconnectHandler> mqtt5DisconnectHandlerLazy,
            final Lazy<Mqtt3DisconnectHandler> mqtt3DisconnectHandlerLazy) {

        switch (clientData.getMqttVersion()) {
            case MQTT_5_0:
                return mqtt5DisconnectHandlerLazy.get();
            case MQTT_3_1_1:
                return mqtt3DisconnectHandlerLazy.get();
            default:
                throw new IllegalStateException();
        }
    }

    @Provides
    @ConnectionScope
    @Named("Auth")
    static ChannelHandler provideAuthHandler(
            final MqttConnect connect, final Lazy<MqttAuthHandler> authHandlerLazy,
            final Lazy<MqttDisconnectOnAuthHandler> disconnectOnAuthHandlerLazy) {

        return (connect.getRawEnhancedAuthProvider() == null) ? disconnectOnAuthHandlerLazy.get() :
                authHandlerLazy.get();
    }

}
