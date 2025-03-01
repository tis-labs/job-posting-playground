package dev.jobposting.playground.domain.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.tsid.TsidFactory;

@Configuration
public class SnowflakeConfig {

	@Value("${snowflake.node-id}")
	private int nodeId;

	@Bean
	TsidFactory tsidFactory() {
		if (nodeId < 0 || nodeId > 1023) {
			throw new IllegalArgumentException("nodeId must be between 0 and 1023");
		}
		return new TsidFactory(nodeId);
	}
}
