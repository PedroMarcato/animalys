(function(){
    'use strict';
    function log(){
        try{
            console.log('[debug-upload] loaded. PrimeFaces:', typeof PrimeFaces !== 'undefined' ? 'present' : 'missing', 'jQuery:', typeof $ !== 'undefined' ? 'present' : 'missing');
        }catch(e){/* ignore */}
    }

    function attachToFileUploads(){
        var nodes = document.querySelectorAll('.ui-fileupload');
        nodes.forEach(function(el){
            if(el.__debugAttached) return;
            el.__debugAttached = true;

            // native file input inside PrimeFaces fileUpload has class ui-fileupload-choose
            var input = el.querySelector('input[type=file]');
            if(input){
                input.addEventListener('change', function(e){
                    console.log('[debug-upload] file input change detected', {id: el.id, files: e.target.files});
                });
                input.addEventListener('click', function(e){
                    console.log('[debug-upload] file input click', {id: el.id});
                });
            }

            // also listen for clicks on the whole fileupload component
            el.addEventListener('click', function(e){
                console.log('[debug-upload] .ui-fileupload clicked', {target: e.target, containerId: el.id});
            });
        });
    }

    if(document.readyState === 'loading'){
        document.addEventListener('DOMContentLoaded', function(){ log(); attachToFileUploads(); });
    } else {
        log(); attachToFileUploads();
    }

    // Observe DOM to attach to dynamically added fileupload components
    try{
        var mo = new MutationObserver(function(muts){ attachToFileUploads(); });
        mo.observe(document.body, {childList:true, subtree:true});
    }catch(e){ /* MutationObserver may not be available in very old browsers */ }

    // expose a helper for manual inspection
    window.__debugUpload = {
        attach: attachToFileUploads
    };
})();
