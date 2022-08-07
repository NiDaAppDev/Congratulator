# Congratulator
A handy library to show custom congratulations screen (uses [confetti library](https://github.com/jinatonic/confetti) and [animations library](https://github.com/daimajia/AndroidViewAnimations))

[![](https://jitpack.io/v/NiDaAppDev/Congratulator.svg)](https://jitpack.io/#NiDaAppDev/Congratulator)

### <b><i>Sample App APK</b></i>
[Sample App](https://github.com/NiDaAppDev/Congratulator/raw/master/Congratulator/sample%20app.apk)

## Screenshot

![congratulator_screenshot](https://user-images.githubusercontent.com/30749705/182833793-4b4cb590-65e0-41dd-8332-466c6fcb4030.gif)

## Installation (Add In Dependencies)
	implementation 'com.github.NiDaAppDev:Congratulator:0.1.2'

## Usage
(An example app is attached)\
Here's a code that's showing a CongratulationView:

	CongratulationView congratulator = new CongratulationView.Builder(activity)
				.setTitle(title)
				.setContent(content)
				.setImageRes(imageRes)
				.enableSound(enable)
				.enableImage(isImageEnabled, isImageAnimationEnabled)
				.setConfettiColors(confettiColors)
				.show();
#### Attributes
<code>setTitle(String)</code> Title is the "Congratulation!" in the gif above.\
<code>setContent(String)</code> Content is the "Well Done!" in the gif above.\
<code>setImageRes(int)</code> ImageRes is the resource id of the image (trophy in the gif above).\
<code>enableSound(boolean)</code> Determines whether a sound is played when showing or not.\
<code>enableImage(boolean, boolean)</code> Determines whether the image is showing or not, and whether it's animated or not.\
<code>setConfettiColors(int[])</code> Confetti colors are the colors of the confetti particles in the gif above.

### Future Development
Suggestions and requests are more than welcome, and I'll try to address them as soon as I can.

## License
	MIT License

	Copyright (c) 2022 Nitzan Daloomy

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
