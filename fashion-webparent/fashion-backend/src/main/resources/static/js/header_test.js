var headerFix = function(){
    'use strict';
    /* Main navigation fixed on top  when scroll down function custom */
    jQuery(window).on('scroll', function () {

        if(jQuery('.header').length > 0){
            var menu = jQuery('.header');
            $(window).scroll(function(){
                var sticky = $('.header'),
                    scroll = $(window).scrollTop();

                if (scroll >= 100){ sticky.addClass('sticky');
                }else {sticky.removeClass('sticky');}
            });
        }

    });
    /* Main navigation fixed on top  when scroll down function custom end*/
}