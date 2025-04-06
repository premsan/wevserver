package com.wevserver.poll;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PollReadController {

    private final PollRepository pollRepository;
}
