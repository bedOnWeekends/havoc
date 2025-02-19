package com.bedonweekends.listener;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class Ready extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        log.info("API is ready!");
    }
}
