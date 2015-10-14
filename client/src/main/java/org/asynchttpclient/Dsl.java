/*
 * Copyright (c) 2015 AsyncHttpClient Project. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at
 *     http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.asynchttpclient;

import org.asynchttpclient.Realm.AuthScheme;
import org.asynchttpclient.proxy.ProxyServer;

public final class Dsl {

    // /////////// Client ////////////////
    public static AsyncHttpClient asyncHttpClient() {
        return new DefaultAsyncHttpClient();
    }

    public static AsyncHttpClient asyncHttpClient(DefaultAsyncHttpClientConfig.Builder configBuilder) {
        return new DefaultAsyncHttpClient(configBuilder.build());
    }

    public static AsyncHttpClient asyncHttpClient(AsyncHttpClientConfig config) {
        return new DefaultAsyncHttpClient(config);
    }

    // /////////// ProxyServer ////////////////
    public static ProxyServer.Builder proxyServer(String host, int port) {
        return new ProxyServer.Builder(host, port);
    }

    // /////////// Config ////////////////
    public static DefaultAsyncHttpClientConfig.Builder config() {
        return new DefaultAsyncHttpClientConfig.Builder();
    }

    public static AdvancedConfig.Builder advancedConfig() {
        return new AdvancedConfig.Builder();
    }

    // /////////// Realm ////////////////
    public static Realm.Builder realm(Realm prototype) {
        return new Realm.Builder()//
                .setRealmName(prototype.getRealmName())//
                .setAlgorithm(prototype.getAlgorithm())//
                .setMethodName(prototype.getMethodName())//
                .setNc(prototype.getNc())//
                .setNonce(prototype.getNonce())//
                .setPassword(prototype.getPassword())//
                .setPrincipal(prototype.getPrincipal())//
                .setCharset(prototype.getCharset())//
                .setOpaque(prototype.getOpaque())//
                .setQop(prototype.getQop())//
                .setScheme(prototype.getScheme())//
                .setUri(prototype.getUri())//
                .setUsePreemptiveAuth(prototype.isUsePreemptiveAuth())//
                .setNtlmDomain(prototype.getNtlmDomain())//
                .setNtlmHost(prototype.getNtlmHost())//
                .setUseAbsoluteURI(prototype.isUseAbsoluteURI())//
                .setOmitQuery(prototype.isOmitQuery());
    }

    public static Realm.Builder realm(AuthScheme scheme, String principal, String password) {
        return new Realm.Builder()//
                .setScheme(scheme)//
                .setPrincipal(principal)//
                .setPassword(password);
    }

    public static Realm.Builder basicAuthRealm(String principal, String password) {
        return realm(AuthScheme.BASIC, principal, password);
    }

    public static Realm.Builder digestAuthRealm(String principal, String password) {
        return realm(AuthScheme.DIGEST, principal, password);
    }

    public static Realm.Builder ntlmAuthRealm(String principal, String password) {
        return realm(AuthScheme.NTLM, principal, password);
    }

    private Dsl() {
    }
}
