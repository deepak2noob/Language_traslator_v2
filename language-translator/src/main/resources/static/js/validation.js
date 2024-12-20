document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            let isValid = true;
            
            const username = document.getElementById('username');
            const password = document.getElementById('password');
            
            if (username.value.trim() === '') {
                username.classList.add('is-invalid');
                isValid = false;
            } else {
                username.classList.remove('is-invalid');
            }
            
            if (password.value.trim() === '') {
                password.classList.add('is-invalid');
                isValid = false;
            } else {
                password.classList.remove('is-invalid');
            }
            
            if (!isValid) {
                e.preventDefault();
            }
        });
    }

    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            let isValid = true;
            
            const username = document.getElementById('username');
            const email = document.getElementById('email');
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            
            // Username validation
            if (username.value.trim().length < 3) {
                username.classList.add('is-invalid');
                isValid = false;
            } else {
                username.classList.remove('is-invalid');
            }
            
            // Email validation
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email.value.trim())) {
                email.classList.add('is-invalid');
                isValid = false;
            } else {
                email.classList.remove('is-invalid');
            }
            
            // Password validation
            const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
            if (!passwordRegex.test(password.value)) {password.classList.add('is-invalid');
                isValid = false;
            } else {
                password.classList.remove('is-invalid');
            }
            
            // Confirm password validation
            if (password.value !== confirmPassword.value) {
                confirmPassword.classList.add('is-invalid');
                isValid = false;
            } else {
                confirmPassword.classList.remove('is-invalid');
            }
            
            if (!isValid) {
                e.preventDefault();
            }
        });

        // Real-time validation
        const inputs = registerForm.querySelectorAll('input');
        inputs.forEach(input => {
            input.addEventListener('input', function() {
                if (input.id === 'username') {
                    if (input.value.trim().length >= 3) {
                        input.classList.remove('is-invalid');
                    }
                } else if (input.id === 'email') {
                    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    if (emailRegex.test(input.value.trim())) {
                        input.classList.remove('is-invalid');
                    }
                } else if (input.id === 'password') {
                    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
                    if (passwordRegex.test(input.value)) {
                        input.classList.remove('is-invalid');
                    }
                    // Update confirm password validation
                    const confirmPassword = document.getElementById('confirmPassword');
                    if (confirmPassword.value) {
                        if (input.value === confirmPassword.value) {
                            confirmPassword.classList.remove('is-invalid');
                        } else {
                            confirmPassword.classList.add('is-invalid');
                        }
                    }
                } else if (input.id === 'confirmPassword') {
                    const password = document.getElementById('password');
                    if (input.value === password.value) {
                        input.classList.remove('is-invalid');
                    }
                }
            });
        });
    }
});
