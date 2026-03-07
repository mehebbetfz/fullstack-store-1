// Auto-dismiss flash alerts after 4 seconds
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.alert.alert-success, .alert.alert-info').forEach(function (el) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(el);
            if (bsAlert) bsAlert.close();
        }, 4000);
    });

    // Confirm delete actions
    document.querySelectorAll('[data-confirm]').forEach(function (el) {
        el.addEventListener('click', function (e) {
            if (!confirm(el.getAttribute('data-confirm'))) e.preventDefault();
        });
    });

    // Add to cart animation
    document.querySelectorAll('form[action*="/cart/add"] button').forEach(function(btn) {
        btn.addEventListener('click', function() {
            btn.innerHTML = '<i class="bi bi-check"></i>';
            btn.classList.add('btn-success');
            btn.classList.remove('btn-primary');
            setTimeout(() => {
                btn.innerHTML = '<i class="bi bi-cart-plus"></i>';
                btn.classList.remove('btn-success');
                btn.classList.add('btn-primary');
            }, 1000);
        });
    });
});
