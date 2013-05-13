package com.taobao.easyweb.core.request.pipeline;

import com.taobao.easyweb.core.context.Context;

/**
 * User: jimmey/shantong
 * DateTime: 13-5-3 ионГ11:53
 */
public interface Valve {

    public void invoke(Context context) throws Exception;

}
