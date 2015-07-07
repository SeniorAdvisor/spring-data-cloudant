/*
 *
 *  * Copyright 2010-2012 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.springframework.data.cloudant.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.data.config.ParsingUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Created by justinsaul on 6/9/15.
 */
public class CloudantParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        super.doParse(element, builder);
        ParsingUtils.setPropertyValue(builder, element, "account", "account");
        ParsingUtils.setPropertyValue(builder, element, "username", "username");
        ParsingUtils.setPropertyValue(builder, element, "password", "password");
        ParsingUtils.setPropertyValue(builder, element, "database", "database");
        ParsingUtils.setPropertyValue(builder, element, "maxConnections", "maxConnections");
        ParsingUtils.setPropertyValue(builder, element, "connectionTimeout", "connectionTimeout");
    }

    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
        String id = super.resolveId(element, definition, parserContext);
        return StringUtils.hasText(id) ? id : BeanNames.CLOUDANT;

    }
}
