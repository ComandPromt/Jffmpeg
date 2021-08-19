# Jffmpeg

simple console program derived from ffmpeg. GIFs or videos with watermarks can be made easily

Linux: sudo apt install ffmpeg

---

-h  -> Help.

-help  -> Help.

-i  -> Input (path of file).


-ss  -> Time Start 

 	 Option is considered an actual timestamp,

	 and is not offset by the start time of the file. 

	 This matters only for files which do not start

	 from timestamp 0, such as transport streams.


-t  -> Time Duration 

 	 Record or transcode "duration" seconds of audio/video.


-r  -> Frame Rate 

 	 Set frame rate (Hz value, fraction or abbreviation).


-fps  -> Frames Per Second.


-s  -> Set frame size (WxH or abbreviation)


-------------------------Watermarks-------------------------


	-watermark  -> Input (path of watermark file).


	---------------------Position---------------------


	-pos-watermark -> Position of the watermark file. 

		----------------Values----------------

 			0 -> UP - LEFT

			1 -> UP - CENTER

			2 -> UP - RIGHT

			3 -> MIDDLE - LEFT

			4 -> MIDDLE - CENTER

			5 -> MIDDLE - RIGHT

			6 -> DOWN - LEFT

			7 -> DOWN - CENTER

			8 -> DOWN - RIGHT


	---------------------Color---------------------


		--color-watermark -> Watermark text color.

		IMPORTANT: You can put colors in html 

		---------------Values---------------

			black

			white

			red

			blue

			yellow

			lime

			pink

			violet

			gray

			cyan

			darkblue

			lightblue

			purple

			Magenta

			Silver

			orange

			brown

			maroon

			green

			olive

			aquamarine

			gold

			darkorange

			#CFB53B (Old gold)

			chocolate

			#D4AF37 (Metallic gold)

			turquoise

			teal

			tseagreen

			#78866B (Camouflage green)

			#CD7F32(bronze)

			#F3E5AB (Medium champagne)


	---------------------Text---------------------


		-text-watermark -> Watermark text


		---------------------Color Text---------------------


			-font-size-text-watermark -> Watermark Text size


	---------------------Quality---------------------


		-good -> Set good quality


		-bad -> Set low quality

		-fps -> Frames per Second
		 This option is mandatory if the low quality option is used


	---------------------Output---------------------


		-y  -> Overwrite output files


		You can put the output file path directly

		without the -y argument


		You can leave this option without arguments

		and without output file, since

		the program will save the output file

		as "file-output" with its corresponding extension
		
---

