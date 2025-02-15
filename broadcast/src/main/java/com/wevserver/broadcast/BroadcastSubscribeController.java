package com.wevserver.broadcast;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class BroadcastSubscribeController {

    private final BroadcastSubscriber broadcastSubscriber;

    @GetMapping("/broadcast/broadcast-subscribe")
    public ModelAndView broadcastSubscribeGet() {

        final ModelAndView model =
                new ModelAndView("com/wevserver/broadcast/templates/broadcast-subscribe");

        final BroadcastSubscribe broadcastSubscribe = new BroadcastSubscribe();
        broadcastSubscribe.setOpen(broadcastSubscriber.isOpen());

        model.addObject("broadcastSubscribe", broadcastSubscribe);

        return model;
    }

    @PostMapping("/broadcast/broadcast-subscribe")
    public ModelAndView broadcastSubscribePost(
            final @Valid @ModelAttribute("broadcastSubscribe") BroadcastSubscribe
                            broadcastSubscribe,
            final BindingResult bindingResult) {

        final ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/wevserver/broadcast/templates/broadcast-subscribe");
            modelAndView.addObject("broadcastSubscribe", broadcastSubscribe);

            return modelAndView;
        }

        if (broadcastSubscribe.isOpen()) {

            broadcastSubscriber.openConnection();
        } else {

            broadcastSubscriber.closeConnection();
        }

        return new ModelAndView("redirect:/broadcast/broadcast-subscribe");
    }

    @Getter
    @Setter
    private static class BroadcastSubscribe {

        private boolean open;
    }
}
