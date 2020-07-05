

$(document).ready(function() {

    // ##################################################### هندلر قسمت عکس های بک گراند سایت #####################################################
    var InstallBgImages = $(".Install_BG_Image");
    var InstallBGCountener1 = 0;

    $(InstallBgImages).fadeOut();
    $(InstallBgImages[InstallBGCountener1]).fadeIn();

    HandleInstallBackground();

    // حلقه زمانی برای تغییر عکس بکگراند در زمان مشخص
    setInterval(function() {
            HandleInstallBackground();
        },
        12000);

    // تابع برای هندل کردن عکس های بکگراند زمان نصب
    function HandleInstallBackground() {
        var TempTop, TempLeft;

        var Current = InstallBGCountener1;

        switch (Math.floor((Math.random() * 2)) + 1) {
        case 1:
            TempTop = "-19%";
            TempLeft = "-19%";
            $(InstallBgImages[Current]).css({ "left": "0%", "top": "0%" });
            break;
        case 2:
            TempTop = "-1%";
            TempLeft = "-1%";
            $(InstallBgImages[Current]).css({ "left": "-19%", "top": "-19%" });
            break;
        case 3:
            TempTop = "-19%";
            TempLeft = "-1%";
            $(InstallBgImages[Current]).css({ "left": "-19%", "top": "-1%" });
            break;
        case 4:
            TempTop = "-1%";
            TempLeft = "-19%";
            $(InstallBgImages[Current]).css({ "left": "-1%", "top": "-19%" });
            break;
        default:
            return;
        }

        InstallBGCountener1 = InstallBGCountener1 + 1;
        if (InstallBGCountener1 >= $(InstallBgImages).length) {
            InstallBGCountener1 = 0;
        }

        $(InstallBgImages[Current]).fadeIn({ duration: 3000, queue: false });


        $(InstallBgImages[Current]).animate({ "left": TempTop, "top": TempLeft },
            { duration: 13000, queue: false, easing: "linear" });

        setTimeout(function() {
                $(InstallBgImages[Current]).fadeOut({ duration: 3000, queue: false });
            },
            11000);
    }
    // ##################################################### هندلر قسمت عکس های بک گراند سایت #####################################################

});