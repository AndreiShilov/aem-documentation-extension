/* globals $,Granite */
$(window).adaptTo('foundation-registry').register('foundation.validation.validator', {
    selector: 'input[data-validation="required-if-visible"]',
    validate: function (el) {
        var isVisible = $(el).is(':visible');
        if (isVisible && !el.value) {
            return Granite.I18n.get("Required");
        }
    }
});