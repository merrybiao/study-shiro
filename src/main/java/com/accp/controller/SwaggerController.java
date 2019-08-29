package com.accp.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.accp.base.BaseController;

@Controller
@RequestMapping("/system/swagger/docs")
public class SwaggerController extends BaseController {

    @RequiresPermissions("system:swagger:view")
    @GetMapping()
    public String index()
    {
        return redirect("/swagger-ui.html");
    }
}
