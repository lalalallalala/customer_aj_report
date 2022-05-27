package com.anjiplus.template.gaea.business.listener;

import com.anjiplus.template.gaea.business.modules.accessuser.service.AccessUserService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.ruijie.jeef.core.util.eventbus.EventBusContext;
import com.ruijie.jeef.core.util.eventbus.EventListener;
import com.ruijie.jeef.core.util.eventbus.EventObj;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author 林波
 * @date 2022/5/23
 */
@Component
@Slf4j
public class ReportEventBusListener implements EventListener {

    @Resource
    AccessUserService accessUserService;

    private Table<String, String, Consumer> eventTable = HashBasedTable.create();

    private Consumer<EventObj<Map<String, Object>>> onUserLoginConsumer = event -> {
        Map<String, Object> data = event.getData();
        log.error("接收用户登录的消息，eventId: {}, 用户：{}", event.getId(), data);

        if (data == null) {
            return;
        }

        accessUserService.autoLoginForJeef(String.valueOf(data.get("account")), String.valueOf(data.get("name")));
    };

    @PostConstruct
    public void register() {
        EventBusContext.register(this);
        eventTable.put("user", "login", onUserLoginConsumer);
    }

    @Override
    public void handle(EventObj event) {
        log.info("ReportEventBusListener.handle(), {}, {}, {}, {}", event.getId(), event.getTopic(), event.getGroup(), event.getData());
        Consumer consumer = eventTable.get(event.getTopic(), event.getGroup());
        if(consumer == null){
            log.error("ReportEventBusListener.handle(), 没有找到对应的处理器, {}, {}", event.getTopic(), event.getGroup());
            return;
        }
        consumer.accept(event);
    }

}
