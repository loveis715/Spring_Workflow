package com.vmware.workflow.core.activity.segment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.handler.MethodInvokingMessageProcessor;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.support.channel.ChannelResolver;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.HeaderEnricher.HeaderValueMessageProcessor;
import org.springframework.integration.transformer.MessageTransformingHandler;

import com.vmware.workflow.core.activity.WhileActivity;

public class WhileActivitySegment extends ContainerActivitySegment<WhileActivity> {

    WhileActivitySegment(WhileActivity activity) {
        super(activity);
    }

    @Override
    void expandAction(SpringIntegrationContext context) throws Throwable {
        Object conditionBean = activity.getConditionBean();
        String conditionMethod = activity.getConditionMethod();
        ManagedMap<String, HeaderProcessor> headers = new ManagedMap<String, HeaderProcessor>();
        HeaderProcessor processor = new HeaderProcessor(conditionBean, conditionMethod);
        headers.put("condition", processor);

        DirectChannel enricherOutputChannel = new DirectChannel();
        HeaderEnricher enricher = new HeaderEnricher(headers);
        MessageTransformingHandler enrichHandler = new MessageTransformingHandler(enricher);
        enrichHandler.setOutputChannel(enricherOutputChannel);

        EventDrivenConsumer enrichConsumer = new EventDrivenConsumer((SubscribableChannel) inputChannel, enrichHandler);
        enrichConsumer.start();

        DirectChannel bodyChannel = new DirectChannel();
        DirectChannelResolver channelResolver = new DirectChannelResolver();
        channelResolver.addChannel("true", bodyChannel);
        channelResolver.addChannel("false", outputChannel);

        HeaderValueRouter router = new HeaderValueRouter("condition");
        router.setChannelResolver(channelResolver);
        router.setChannelMapping("true", "true");
        router.setChannelMapping("false", "false");

        EventDrivenConsumer routerConsumer = new EventDrivenConsumer(enricherOutputChannel, router);
        routerConsumer.start();

        childrenInputChannel = bodyChannel;
        childrenOutputChannel = inputChannel;
        super.expandAction(context);
    }

    static class DirectChannelResolver implements ChannelResolver {

        private final Map<String, MessageChannel> channelMapper = new HashMap<String, MessageChannel>();

        public void addChannel(String name, MessageChannel channel) {
            channelMapper.put(name, channel);
        }

        @Override
        public MessageChannel resolveChannelName(String name) {
            return channelMapper.get(name);
        }
    }

    static class HeaderProcessor implements HeaderValueMessageProcessor<Boolean> {

        private final MethodInvokingMessageProcessor<Boolean> targetProcessor;

        public HeaderProcessor(Object conditionBean, String conditionMethod) {
            this.targetProcessor = new MethodInvokingMessageProcessor<Boolean>(conditionBean, conditionMethod);
        }

        @Override
        public Boolean processMessage(Message<?> message) {
            return targetProcessor.processMessage(message);
        }

        @Override
        public Boolean isOverwrite() {
            return true;
        }
    }
}