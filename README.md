# Codename One CSS Support

A library to add support for designing Codename One themes using CSS (Cascading Style Sheets).

#Requirements

- Your development machine must have Java 8 installed.

## Features

* .css files compiled to native Codename One .res theme files at compile time.
* Support for all style settings that are supported by the CN1 resource editor:
    * Padding
    * Margin
    * Background
    * Colors (foreground, background, transparency)
    * Fonts (system and TTF)
    * Borders (Standard and 9-piece Image borders)
* Automatic 9-piece border generation where borders and backgrounds cannot be achieved efficiently using standard CN1 styles.
    * Rounded borders (via `border-radius`)
    * Linear Gradients (via `linear-gradient`)
    * Radial Gradients (via `radial-gradient`)
    * Opacity
    * Box Shadow (via `box-shadow`)
* Automatic image imports as Multi-images.
* Automatic TTF font imports via the `@font-face` directive.

## Installation

1. Download cn1css-ant-task.jar and copy into your project's `lib` directory.
2. Copy the following snippet into you project's `build.xml` file:
 ~~~~
    <taskdef name="compileCSS"
        classname="com.codename1.ui.css.CN1CSSCompileTask"
        classpath="lib/cn1css-ant-task.jar"/>
    
    <target name="compile-css">
        <compileCSS/>
    </target>
 ~~~~
3. Change the following line in your project's `build.xml` file:
 
 ~~~~
 <target name="-pre-compile">
 ~~~~
 
 to
 
 ~~~~
 <target name="-pre-compile" depends="compile-css">
 ~~~~
 
 
## Usage / How it Works

Once you install the above snippet into your build.xml file, it will activate the CSS compiler to run each time you compile your project.  The CSS compiler will look for .css files inside your project's "css" directory (which you'll need to create).  It will compiles these CSS files into corresponding `.res` files which will be placed into your project's `src` directory.  You can then load this theme in your app just as you would load any other theme file.  E.g.

Suppose you add a CSS file into your project  `css/theme.css`.  When you compile your project, it will generate the file `src/theme.css.res`.  Then you can load this file in your app as follows:

~~~~
Resources css = Resources.openLayered("/theme.css");
UIManager.getInstance().addThemeProps(css.getThemeResourceNames()[0]);
~~~~

## Supported CSS Directives

* `padding`  (and variants)
* `margin` (and variants)
* `border` (and variants)
* `border-radius`
* `background`
* `background-color`
* `background-repeat`
* `background-image`
* `font`
* `font-family`
* `font-style`
* `font-size`
* `@font-face`
* `color`
* `text-align`
* `opacity`
* `box-shadow`
* `width`  (only used for generating background-images and borders)
* `height` (only used for generating background-images and borders)

## Custom Codename One CSS Directives

* `cn1-source-dpi` - Used to specify source DPI for multi-image generation of background images.
* `cn1-background-type` - Used to explicitly specify the background-type that should be used for the class.
* `cn1-9patch` - Used to explicitly specify the slices used when generating 9-piece borders.
* `cn1-derive` - Used to specify that this UIID should derive from an existing UIID.


## Supported CSS Selectors

There are 3 variants of selectors that you can use in your CSS files:

1. **UIID Name** - If you add a selector without specifying a class or ID, it will be interpreted as a UIID.  E.g.:
 ~~~~
 Form { /* Styles applied to the "Form" style/UIID */
      
 }
 
 Foo {  /* Creates style/UIID named "Foo" */
 
 }
 ~~~~
2. **Predefined CSS Classes** - `.pressed`, `selected`, `.unselected`, `.disabled` E.g.:
 ~~~~
 Button.selected { /** Styles applied to the Button style/UIID in "selected" state.*/
 
 }
 
 Button.pressed { /** Styles applied to Button style/UIID in "pressed" state*/
 
 }
 
 Button { /** Styles applied to *ALL* states of Button style/UIID */
 
 }
 ~~~~
3. **All Styles** - You can use the `*` selector to apply styling to all UIIDs (that are defined in the same CSS file.  E.g.:
 ~~~~
 * {/** Styles applied to all UIIDs **/
 
 }
 ~~~~
4. **Default Element** - You can use the UIID `Default` to specify styles that should be set for the default element in the theme.  E.g.:
 ~~~~
 Default {/** Styles applied to the default element of the theme. */
 
 }
 ~~~~
5. **`#Device`** - You can use the `#Device` selector to configure some specific properties about the target devices for this theme.  E.g.:
 ~~~~
 #Device {
     min-resolution: 120dpi;
     max-resolution: 480dpi;
     resolution: 480dpi;
 }
 ~~~~
6. **`#Constants`** - You can specify theme constants in this directive.  Constants will have the same name as their corresponding constants in a Codename One Theme as shown in the resource editor.  Here is a sample section from a stylesheet that sets all of the default constants.
 ~~~~
 #Constants {
     PopupDialogArrowBool: false;
     calTitleDayStyleBool: true;
     calTransitionVertBool: false;
     calendarLeftImage: "cal_left_arrow.png";
     calendarRightImage: "cal_right_arrow.png";
     centeredPopupBool: false;
     checkBoxCheckDisFocusImage: "Check-Box_Normal.png";
     checkBoxCheckedFocusImage: "Check-Box_Press.png";
     checkBoxCheckedImage: "Check-Box_Press.png";
     checkBoxOppositeSideBool: true;
     checkBoxUncheckedFocusImage: "Check-Box_Normal.png";
     checkBoxUncheckedImage: "Check-Box_Normal.png";
     comboImage: "combo.png";
     commandBehavior: "Side";
     dialogTransitionIn: "fade";
     dialogTransitionOut: "fade";
     dlgButtonCommandUIID: "DialogButton";
     dlgCommandGridBool: true;
     dlgInvisibleButtons: #1a1a1a;
     formTransitionIn: "empty";
     formTransitionOut: "slide";
     includeNativeBool: true;
     menuImage: "of_menu.png";
     noTextModeBool: true;
     onOffIOSModeBool: true;
     otherPopupRendererBool: false;
     pureTouchBool: true;
     radioSelectedFocusImage: "Radio_btn_Press.png";
     radioSelectedImage: "Radio_btn_Press.png";
     radioUnselectedFocusImage: "Radio_btn_Normal.png";
     radioUnselectedImage: "Radio_btn_Normal.png";
     sideMenuImage: "menu.png";
     switchMaskImage: "switch_mask.png";
     switchOffImage: "switch_off.png";
     switchOnImage: "switch_on.png";
     tabPlacementInt: 0;
     backIconImage: "Back-icon.png";
     articleSourceIconImage: "Source-icon.png";
     articleDateIconImage: "Date-icon.png";
     articleArrowRightImage: "Arrow-right.png";
     articleShareIconImage: "Share-icon.png";
     articleBookmarkIconImage: "Bookmark-icon.png";
     articleTextIconImage: "Text-icon.png";
     articleCommentsIconImage: "Comments-icon.png";
     newsIconImage: "News-icon.png";
     channelsIconImage: "Channels-icon.png";
     bookmarksIconImage: "Bookmarks-icon.png";
     overviewIconImage: "Overview-icon.png";
     calendarIconImage: "Calendar-icon.png";
     timelineIconImage: "Timeline-icon.png";
     profileIconImage: "Profile-icon.png";
     widgetsIconImage: "Widgets-icon.png";
     settingsIconImage: "Settings-icon.png";
     
 }
 ~~~~


## Examples

* [Test CSS File](https://github.com/shannah/cn1-css/blob/master/cn1-css-demo/css/test1.css) is compiled to [this Codename One theme file](https://github.com/shannah/cn1-css/blob/master/cn1-css-demo/src/test1.css.res?raw=true)

## Building From Source

~~~~
$ git clone https://github.com/shannah/cn1-css.git
$ cd cn1-css/CN1CSSCompiler
$ ant jar
~~~~

If all went well, you'll find the generated `cn1css-ant-task.jar` inside the `cn1-css/CN1CSSCompiler/dist` directory.



