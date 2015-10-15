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

[See the Installation Wiki Page](https://github.com/shannah/cn1-css/wiki/Installation) 
 
## Usage / How it Works

Once you install the above snippet into your build.xml file, it will activate the CSS compiler to run each time you compile your project.  The CSS compiler will look for .css files inside your project's "css" directory (which you'll need to create).  It will compiles these CSS files into corresponding `.res` files which will be placed into your project's `src` directory.  You can then load this theme in your app just as you would load any other theme file.  E.g.

Suppose you add a CSS file into your project  `css/theme.css`.  When you compile your project, it will generate the file `src/theme.css.res`.  Then you can load this file in your app as follows:

~~~~
Resources css = Resources.openLayered("/theme.css");
UIManager.getInstance().addThemeProps(css.getThemeResourceNames()[0]);
~~~~

## Supported CSS Directives

[See the Supported CSS Properties Wiki Page](https://github.com/shannah/cn1-css/wiki/Supported-Properties)

## Supported CSS Selectors

[See the Supported Selectors Wiki Page](https://github.com/shannah/cn1-css/wiki/Supported-CSS-Selectors)

## Image Support

[See the Images Wiki Page](https://github.com/shannah/cn1-css/wiki/Images) :

* [Importing Multiple Images in a Single Selector](https://github.com/shannah/cn1-css/wiki/Images#import-multiple-images-in-single-selector)
* [Loading Images Remotely](https://github.com/shannah/cn1-css/wiki/Images#loading-images-from-urls)
* [Generating 9-Piece Image Borders](https://github.com/shannah/cn1-css/wiki/Images#generating-9-piece-image-borders)
* [Image Backgrounds](https://github.com/shannah/cn1-css/wiki/Images#image-backgrounds)
* [Image Compression](https://github.com/shannah/cn1-css/wiki/Images#image-compression)

## Font Support 

[See the Fonts Wiki Page](https://github.com/shannah/cn1-css/wiki/Fonts)


## Examples

* [Test CSS File](https://github.com/shannah/cn1-css/blob/master/cn1-css-demo/css/test1.css) is compiled to [this Codename One theme file](https://github.com/shannah/cn1-css/blob/master/cn1-css-demo/src/test1.css.res?raw=true)

## Building From Source

~~~~
$ git clone https://github.com/shannah/cn1-css.git
$ cd cn1-css/CN1CSSCompiler
$ ant jar
~~~~

If all went well, you'll find the generated `cn1css-ant-task.jar` inside the `cn1-css/CN1CSSCompiler/dist` directory.



