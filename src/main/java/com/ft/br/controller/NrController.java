package com.ft.br.controller;

import com.ft.br.model.dto.ValidParent;

import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * NrController
 *
 * @author shichunyang
 */
@RestController
@CrossOrigin
@Slf4j
public class NrController {

    @Autowired
    private SsoService ssoService;

    @Autowired
    private ExecutorService executorService;

    @PutMapping("/valid-model")
    public String valid(@RequestBody @Valid ValidParent validParent) {
        return JsonUtil.object2Json(validParent);
    }

    @PostMapping("/upload")
    public String upload(
            @RequestParam String username,
            @RequestParam Integer age,
            @RequestParam MultipartFile excel
    ) throws Throwable {
        try (InputStream in = excel.getInputStream()) {
            return JsonUtil.object2Json(ExcelUtil.readExcel(in, false, "#"));
        }
    }

    /**
     * mysql死锁:使用in (1, 2)解决。
     */
    @GetMapping("/deadLock")
    public RestResult<Boolean> deadLock() {
        executorService.submit(() -> ssoService.deadLock(1, 2));
        executorService.submit(() -> ssoService.deadLock(2, 1));
        return RestResult.success(Boolean.TRUE);
    }
}
