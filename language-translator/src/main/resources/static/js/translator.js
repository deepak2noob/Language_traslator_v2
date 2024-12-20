document.addEventListener('DOMContentLoaded', function() {
    const translatorForm = document.getElementById('translatorForm');
    const inputText = document.getElementById('inputText');
    const outputText = document.getElementById('outputText');
    const fromLang = document.getElementById('fromLang');
    const toLang = document.getElementById('toLang');

    translatorForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        if (!inputText.value.trim()) {
            inputText.classList.add('is-invalid');
            return;
        }

        try {
            // Show loading state
            const submitButton = translatorForm.querySelector('button[type="submit"]');
            const originalButtonText = submitButton.innerHTML;
            submitButton.disabled = true;
            submitButton.innerHTML = 'Translating...';
            
            // Prepare the query parameters
            const params = new URLSearchParams({
                text: inputText.value.trim(),
                fromLang: fromLang.value,
                toLang: toLang.value
            });

            // Make the translation request
            const response = await fetch(`/translate?${params.toString()}`);
            const data = await response.json();

            // Update the output
            if (data.responseData && data.responseData.translatedText) {
                outputText.textContent = data.responseData.translatedText;
            } else {
                throw new Error('Translation failed');
            }

        } catch (error) {
            console.error('Translation error:', error);
            outputText.textContent = 'Error: Could not perform translation. Please try again.';
            
        } finally {
            // Reset button state
            submitButton.disabled = false;
            submitButton.innerHTML = originalButtonText;
        }
    });

    // Real-time validation for input text
    inputText.addEventListener('input', function() {
        if (this.value.trim()) {
            this.classList.remove('is-invalid');
        }
    });

    // Swap languages functionality
    document.getElementById('swapLangs')?.addEventListener('click', function() {
        const fromValue = fromLang.value;
        fromLang.value = toLang.value;
        toLang.value = fromValue;
    });
});