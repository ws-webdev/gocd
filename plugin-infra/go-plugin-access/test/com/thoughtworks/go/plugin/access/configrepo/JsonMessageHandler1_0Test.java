/*
 * Copyright 2017 ThoughtWorks, Inc.
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
 */

package com.thoughtworks.go.plugin.access.configrepo;


import com.thoughtworks.go.plugin.access.configrepo.contract.CRParseResult;
import org.junit.Test;

import static com.thoughtworks.go.util.TestUtils.contains;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonMessageHandler1_0Test {

    private final JsonMessageHandler1_0 handler;

    public JsonMessageHandler1_0Test()
    {
        handler = new JsonMessageHandler1_0();
    }

    @Test
    public void shouldErrorWhenMissingTargetVersionInResponse()
    {
        String json = "{\n" +
                "  \"environments\" : [],\n" +
                "  \"pipelines\" : [],\n" +
                "  \"errors\" : []\n" +
                "}";

        CRParseResult result = handler.responseMessageForParseDirectory(json);
        assertThat(result.getErrors().getErrorsAsText(),contains("missing 'target_version' field"));
    }

    @Test
    public void shouldNotErrorWhenTargetVersionInResponse()
    {
        String json = "{\n" +
                "  \"target_version\" : 1,\n" +
                "  \"pipelines\" : [],\n" +
                "  \"errors\" : []\n" +
                "}";

        CRParseResult result = handler.responseMessageForParseDirectory(json);
        assertFalse(result.hasErrors());
    }

    @Test
    public void shouldAppendPluginErrorsToAllErrors()
    {
        String json = "{\n" +
                "  \"target_version\" : 1,\n" +
                "  \"pipelines\" : [],\n" +
                "  \"errors\" : [{\"location\" : \"somewhere\", \"message\" : \"failed to parse pipeline.json\"}]\n" +
                "}";
        CRParseResult result = handler.responseMessageForParseDirectory(json);
        assertTrue(result.hasErrors());
    }
}
