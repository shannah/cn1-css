# Codename One CSS Support

A library to add support for designing Codename One themes using CSS (Cascading Style Sheets).

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

* Download cn1css-ant-task.jar and copy into your project's `lib` directory.
* Copy the following snippet into you project's `build.xml` file:
 ~~~~
    <taskdef name="compileCSS"
        classname="com.codename1.ui.css.CN1CSSCompileTask"
        classpath="lib/cn1css-ant-task.jar"/>
    
    <target name="compile-css">
        <compileCSS/>
    </target>
 ~~~~
* Change the following line in your project's `build.xml` file:
 ~~~~
 <target name="-post-compile">
 ~~~~
 to
 ~~~~
 <target name="-post-compile" depends="compile-css">
 ~~~~
 
## Usage / How it Works

Once you install the above snippet into your build.xml file, it will activate the CSS compiler to run each time you compile your project.  The CSS compiler will look for .css files inside your project's "css" directory (which you'll need to create).  It will compiles these CSS files into corresponding `.res` files which will be placed into your project's `src` directory.  You can then load this theme in your app just as you would load any other theme file.  E.g.

Suppose you add a CSS file into your project  `css/theme.css`.  When you compile your project, it will generate the file `src/theme.css.res`.  Then you can load this file in your app as follows:

~~~~
Resources css = Resources.openLayered("/theme.css");
~~~~

Here is an example `init()` method that loads your app's default theme file, then layers the styles in your `theme.css` file over top of it:

~~~~
public class CSSDemo {

    private Resources theme;
    private Resources css;

    public void init(Object context) {
        try {
            theme = Resources.openLayered("/theme");
            css = Resources.openLayered("/theme.css");
            Hashtable vals = theme.getTheme(theme.getThemeResourceNames()[0]);
            vals.putAll(css.getTheme(css.getThemeResourceNames()[0]));
            UIManager.getInstance().setThemeProps(vals);
            
        } catch(IOException e){
            e.printStackTrace();
        }
        
    }
    ....
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
 
 Button.pressed { /** Styles applied to Button style/UIID in "pressed" state
 
 }
 
 Button { /** Styles applied to *ALL* states of Button style/UIID */
 
 }
 ~~~~
3. **All Styles** - You can use the `*` selector to apply styling to all UIIDs (that are defined in the same CSS file.  E.g.:
 ~~~~
 * {/** Styles applied to all UIIDs **/
 
 }
 ~~~~

