package com.github.jbox.trace.slots;

import com.github.jbox.slot.JobSlot;
import com.github.jbox.trace.TraceException;
import com.github.jbox.trace.TraceSlotContext;
import com.taobao.csp.sentinel.Entry;
import com.taobao.csp.sentinel.SphU;
import com.taobao.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static com.github.jbox.utils.JboxUtils.getSimplifiedMethodName;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2018-07-26 08:33:00.
 */
public class SentinelSlot implements JobSlot<TraceSlotContext> {

    private static final long serialVersionUID = -457074399163946394L;

    private static final Logger rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    @Override
    public void invoke(TraceSlotContext context) throws Throwable {
        Entry entry = null;
        Method method = context.getMethod();
        try {
            if (!Modifier.isPrivate(method.getModifiers()) && !Modifier.isProtected(method.getModifiers())) {
                entry = SphU.entry(method);
            }
            context.next();
        } catch (BlockException e) {
            String msg = "method: [" + getSimplifiedMethodName(method) + "] invoke was blocked by sentinel.";
            rootLogger.warn(msg, e);
            throw new TraceException(msg, e);
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }
}
