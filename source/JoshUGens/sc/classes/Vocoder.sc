// car is "pitch you hear" (like a saw wave, or sound), mod is voice, num is number of bands
// to be spaced over the threshhold, , hpf and scale add back some of mod
Vocoder {
	
	
	
	*ar { 	arg car, mod, num, low=100, high=5000, q=0.02, hpf=5000, hpfscal=0.05, outscal=25, smooth = 10, spread = 0, center = 0, distribution = 0, cLPF=LPF, cBPF=BPF, cHPF=HPF;
	
			var width, cf, hf,  out, filtmod, filtcar, tracker, ratio;
	
	
		out = Splay.arFill(( num + 1 ), { arg i; 
			
          switch (distribution, 
            0, {
              if (i % 0 == 0) {
                i = num - i;
              };
            },
            1, {
            },
            2, {
              i = num - i;
            }
          );

					ratio = (( high / low)**num.reciprocal );
	
				 	cf =  ( ratio**i) * low;
				 	
					filtmod = cBPF.ar( mod, cf, q);
				
					tracker = Amplitude.kr(filtmod);

          tracker = cLPF.kr(tracker, smooth);
				
					filtcar = cBPF.ar( car, cf, q);	
				
	 
					( outscal * ( filtcar * tracker ));
					},spread,1,center);

		hf = cHPF.ar(HPF.ar( mod, hpf), hpf);

		
		^out + ( hpfscal * hf )}

	*bark {arg signal, input, mull = 10, cBPF=BPF;

                var sourceAmp, freqArray, bwArray;

                freqArray = [50, 150, 250, 350, 450, 570, 700, 840, 1000,
						1170, 1370, 1600, 1850,
					2150, 2500, 2900, 3400, 4000,
						4800, 5800, 7000, 8500, 10500, 13500];
                sourceAmp = Amplitude.kr(input);
                bwArray =
						[50,50,50,50,60,70,80,80,100,110,130,150,180,200,250,400,500,600,700,
					1000,1500,2000,3500,3500];
                ^Mix.arFill ( freqArray.size, {arg i;
                        var output, freq, bandWidth;
                        freq = freqArray.at(i);
                        bandWidth = bwArray.at(i);
                        output = cBPF.ar(signal, freqArray.at(i),bandWidth/freq, sourceAmp)*mull
                })
        }
     }
