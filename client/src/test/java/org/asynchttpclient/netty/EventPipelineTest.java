/*
 * Copyright (c) 2010-2012 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

package org.asynchttpclient.netty;

import static org.asynchttpclient.Dsl.*;
import static org.testng.Assert.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpMessage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.asynchttpclient.AbstractBasicTest;
import org.asynchttpclient.AdvancedConfig;
import org.asynchttpclient.AdvancedConfig.AdditionalPipelineInitializer;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.testng.annotations.Test;

public class EventPipelineTest extends AbstractBasicTest {

    @Test(groups = { "standalone", "netty_provider" })
    public void asyncPipelineTest() throws Exception {

        AdvancedConfig advancedConfig = advancedConfig().httpAdditionalPipelineInitializer(new AdditionalPipelineInitializer() {
            public void initPipeline(ChannelPipeline pipeline) throws Exception {
                pipeline.addBefore("inflater", "copyEncodingHeader", new CopyEncodingHandler());
            }
        }).build();

        try (AsyncHttpClient p = asyncHttpClient(config().setAdvancedConfig(advancedConfig).build())) {
            final CountDownLatch l = new CountDownLatch(1);
            Request request = new RequestBuilder("GET").setUrl(getTargetUrl()).build();
            p.executeRequest(request, new AsyncCompletionHandlerAdapter() {
                @Override
                public Response onCompleted(Response response) throws Exception {
                    try {
                        assertEquals(response.getStatusCode(), 200);
                        assertEquals(response.getHeader("X-Original-Content-Encoding"), "<original encoding>");
                    } finally {
                        l.countDown();
                    }
                    return response;
                }
            }).get();
            if (!l.await(TIMEOUT, TimeUnit.SECONDS)) {
                fail("Timeout out");
            }
        }
    }

    private static class CopyEncodingHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object e) {
            if (e instanceof HttpMessage) {
                HttpMessage m = (HttpMessage) e;
                // for test there is no Content-Encoding header so just hard
                // coding value
                // for verification
                m.headers().set("X-Original-Content-Encoding", "<original encoding>");
            }
            ctx.fireChannelRead(e);
        }
    }
}
