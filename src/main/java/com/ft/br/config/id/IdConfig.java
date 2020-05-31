package com.ft.br.config.id;

import com.ft.util.plugin.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IdConfig
 *
 * @author shichunyang
 */
@Configuration("com.ft.br.config.id.IdConfig")
public class IdConfig {

    @Bean("com.ft.util.plugin.Snowflake")
    public Snowflake snowflake() {
        return new Snowflake(0);
    }
}
