# Congratulator
A handy library to show custom congratulations screen

[![](https://jitpack.io/v/NiDaAppDev/Congratulator.svg)](https://jitpack.io/#NiDaAppDev/Congratulator)

### <b><i>Sample App APK</b></i>
[Sample App]([https://github.com/NiDaAppDev/Congratulator/blob/master/Congratulator/sample%20app.apk](https://github.com/NiDaAppDev/Congratulator/raw/master/Congratulator/sample%20app.apk))

## Screenshot

![congratulator_screenshot](https://user-images.githubusercontent.com/30749705/182738184-864bd340-3632-40f8-b28d-f4feaf5b2c3b.gif)

## Installation (Add In Dependencies)
	implementation 'com.github.NiDaAppDev:Congratulator:0.1.0'

## Usage
(An example app is attached)\
Here's a code that's showing a CongratulationView:

	CongratulationView congratulator = new CongratulationView.Builder(this)
                        .setTitle(title)
                        .setContent(content)
                        .enableSound(enable)
                        .setConfettiColors(confettiColors)
                        .show();
#### Attributes
<code>setTitle(String)</code> Title is the "Congratulation!" in the gif above.\
<code>setContent(String)</code> Content is the "Well Done!" in the gif above.\
<code>enableSound(boolean)</code> Determines if a sound is played when showing.\
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
