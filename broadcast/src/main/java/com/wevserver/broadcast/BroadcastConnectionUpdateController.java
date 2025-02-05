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
public class BroadcastConnectionUpdateController {

    private final BroadcastClientManager broadcastClientManager;

    @GetMapping("/broadcast/connection-update")
    public ModelAndView broadcastConnectionGet() {

        final ModelAndView model =
                new ModelAndView("com/wevserver/broadcast/templates/broadcast-connection-update");

        final BroadcastConnectionUpdate broadcastConnectionUpdate = new BroadcastConnectionUpdate();
        broadcastConnectionUpdate.setOpen(broadcastClientManager.isOpen());

        model.addObject("broadcastConnectionUpdate", broadcastConnectionUpdate);

        return model;
    }

    @PostMapping("/broadcast/connection-update")
    public ModelAndView broadcastConnectionPost(
            final @Valid @ModelAttribute("broadcastConnectionUpdate") BroadcastConnectionUpdate
                            broadcastConnectionUpdate,
            final BindingResult bindingResult) {

        final ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName(
                    "com/wevserver/broadcast/templates/broadcast-connection-update");
            modelAndView.addObject("broadcastConnectionUpdate", broadcastConnectionUpdate);

            return modelAndView;
        }

        if (broadcastConnectionUpdate.isOpen()) {

            broadcastClientManager.openConnection();
        } else {

            broadcastClientManager.closeConnection();
        }

        return new ModelAndView("redirect:/broadcast/connection-update");
    }

    @Getter
    @Setter
    private static class BroadcastConnectionUpdate {

        private boolean open;
    }
}
