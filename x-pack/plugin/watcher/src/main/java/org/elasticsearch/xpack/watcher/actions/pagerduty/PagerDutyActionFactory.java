/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.watcher.actions.pagerduty;

import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.xpack.core.watcher.actions.ActionFactory;
import org.elasticsearch.xpack.watcher.common.text.TextTemplateEngine;
import org.elasticsearch.xpack.watcher.notification.pagerduty.PagerDutyService;

import java.io.IOException;

public class PagerDutyActionFactory extends ActionFactory {

    private final TextTemplateEngine templateEngine;
    private final PagerDutyService pagerDutyService;

    public PagerDutyActionFactory(Settings settings, TextTemplateEngine templateEngine, PagerDutyService pagerDutyService) {
        super(Loggers.getLogger(ExecutablePagerDutyAction.class, settings));
        this.templateEngine = templateEngine;
        this.pagerDutyService = pagerDutyService;
    }

    @Override
    public ExecutablePagerDutyAction parseExecutable(String watchId, String actionId, XContentParser parser) throws IOException {
        PagerDutyAction action = PagerDutyAction.parse(watchId, actionId, parser);
        pagerDutyService.getAccount(action.event.account);
        return new ExecutablePagerDutyAction(action, actionLogger, pagerDutyService, templateEngine);
    }
}
