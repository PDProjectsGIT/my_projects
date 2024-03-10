using System;
using System.Drawing;
using System.Threading.Tasks;
using System.Diagnostics;
using System.Drawing.Imaging;
using System.Runtime.InteropServices;

// Importing a DLL in C#
using VignetteGeneratorCSDLL;

namespace Winietowanie
{
    /**
     * Class representing the control object for the vignette production process
     */
    public class VignetteProcessor
    {
        // Bitmap of the loaded image
        private Bitmap _loadedImage;

        // Bitmap of the loaded image with applied vignette
        private Bitmap _processedImage;

        // Number of threads performing the task
        private int _threadNumber;

        // Vignette width radius coefficient
        private double _radiusParam;

        // Vignette intensity coefficient
        private double _vignetteIntensity;

        // Red color correction value
        private int _vignetteRed;

        // Green color correction value
        private int _vignetteGreen;

        // Blue color correction value
        private int _vignetteBlue;

        // Object used for measuring time
        readonly private Stopwatch _stopwatch;

        /**
         * External method with implemented vignetting algorithm written in low-level language (MASM)
         */
        [DllImport(@"..\..\..\x64\Release\VignetteASM.dll")] //..\..\..\x64\Release\
        static extern float GenerateASM(int[] pixelBuffer, int startIndex, int endIndex, int width, int centerX, int centerY, int red, int green, int blue, float maxDistanse, float vignetteIntensity);

        public VignetteProcessor()
        {

            _stopwatch = new Stopwatch();

            _threadNumber = 1;

            _radiusParam = 4;

            _vignetteIntensity = 0;

            _vignetteRed = 0;

            _vignetteGreen = 0;

            _vignetteBlue = 0;

        }

        public int ImageWidth
        {
            get
            {
                if (IsImageNull(_loadedImage)) return 0;
                return _loadedImage.Width;
            }
        }

        public int ImageHeight
        {
            get { 
                if(IsImageNull(_loadedImage)) return 0;
                return _loadedImage.Height; 
            }
        }

        public int ThreadNumber
        {
            get { return _threadNumber; }
            set
            {
                if (value >= 1)
                {
                    _threadNumber = value;
                }
                else
                {
                    throw new VignetteException("Thread number must be between 1 and 64.");
                }
            }
        }

        public double Radius
        {
            get { return _radiusParam; }
            set {
                if (value >= 0.0 && value <= 4.0)
                {
                    _radiusParam = value;
                }
                else
                {
                    throw new VignetteException("Radius must be between 0.0 and 1.0.");
                }
            }
        }

        public double Intensity
        {
            get { return _vignetteIntensity; }
            set
            {
                if (value >= 0.0 && value <= 1.0)
                {
                    _vignetteIntensity = value;
                }
                else
                {
                    throw new VignetteException("Intensity must be between 0.0 and 1.0.");
                }
            }
        }

        public int VignetteRed
        {
            get { return _vignetteRed; }
            set
            {
                if (value >= 0 && value <= 255)
                {
                    _vignetteRed = value;
                }
                else
                {
                    throw new VignetteException("VignetteRed must be in the range of 0 to 255.");
                }
            }
        }

        public int VignetteGreen
        {
            get { return _vignetteGreen; }
            set
            {
                if (value >= 0 && value <= 255)
                {
                    _vignetteGreen = value;
                }
                else
                {
                    throw new VignetteException("VignetteGreen must be in the range of 0 to 255.");
                }
            }
        }

        public int VignetteBlue
        {
            get { return _vignetteBlue; }
            set
            {
                if (value >= 0 && value <= 255)
                {
                    _vignetteBlue = value;
                }
                else
                {
                    throw new VignetteException("VignetteBlue must be in the range of 0 to 255.");
                }
            }
        }

        /**
         * Method checking if the Bitmap object exists.
         */
        private bool IsImageNull(Bitmap image)
        {
            return image == null;
        }

        /**
         * Method returning the bitmap of the image with vignette
         */
        public Bitmap GetProccesedImage() {

            if (IsImageNull(_processedImage)) 
                throw new VignetteException("The file has not been generated yet.");

            return _processedImage; 
        }

        /**
         * Method saving the bitmap to a file at the specified path
         */
        public void SaveVignetteToFile(String outputPath)
        {
            try
            {
                _processedImage.Save(outputPath, System.Drawing.Imaging.ImageFormat.Png);
            }
            catch(VignetteException ex)
            {
                throw new VignetteException(ex.Message);
            }
            catch (Exception ex)
            {
                throw new VignetteException("An error occurred while trying to save the file.", ex);
            }

        }

        /**
         * Method loading a file from the specified path
         */
        public void LoadImage(String path)
        {
            try
            {
                _loadedImage = new Bitmap(path);
            }
            catch (System.IO.FileNotFoundException ex)
            {
                throw new VignetteException("File not found.", ex);
            }
            catch (System.ArgumentException ex)
            {
                throw new VignetteException("Image loading error.", ex);
            }
            catch (Exception ex)
            {
                throw new VignetteException("Unexpected image loading error.", ex);
            }

        }

        /**
         * Method resetting data
         */

        public void eraseData()
        {
            _loadedImage = null;
            _processedImage = null;
        }

        /**
         * Method returning the execution time (as a string)
         */
        public String getExecutionTime()
        {
            if (_stopwatch == null) 
                throw new VignetteException("The generator have not been excecuted yet.");

            return getExecutionsValue() + " ms";
        }

        /**
         * Method returning the execution time (int)
         */
        public double getExecutionsValue()
        {
            if (_stopwatch == null)
                throw new VignetteException("The generator have not been excecuted yet.");

            // Pobierz upłynięty czas w mikrosekundach
            double elapsedMicroseconds = _stopwatch.Elapsed.Ticks / (TimeSpan.TicksPerMillisecond / 1000.0);

            // Zaokrąglij do 3 miejsc po przecinku
            return Math.Round(elapsedMicroseconds / 1000.0, 3);
        }

        /**
         * Method generating vignette using a low-level library algorithm
         */
        public void GenerateVignetteASM()
        {

            if (IsImageNull(_loadedImage))
                throw new VignetteException("The image has not been loaded yet.");

            int width = _loadedImage.Width;
            int height = _loadedImage.Height;

            BitmapData sourceData = _loadedImage.LockBits(new Rectangle(0, 0, width, height),
                ImageLockMode.ReadOnly, PixelFormat.Format32bppArgb);

            byte[] pixelBufferByte = new byte[sourceData.Stride * sourceData.Height];

            Marshal.Copy(sourceData.Scan0, pixelBufferByte, 0, pixelBufferByte.Length);

            _loadedImage.UnlockBits(sourceData);

            int centerX = width / 2;
            int centerY = height / 2;

            float maxDistance = (float)(Math.Max(centerX, centerY) * _radiusParam);

            int[] pixelBuffer = new int[pixelBufferByte.Length];

            for (int i = 0; i < pixelBuffer.Length; i++)
            {
                pixelBuffer[i] = (int)(pixelBufferByte[i]);
            }

            _stopwatch.Reset();
            _stopwatch.Start();

            Parallel.For(0, _threadNumber, threadIndex => {

                int startIndex = threadIndex * (pixelBuffer.Length / 4) / _threadNumber * 4;
                int endIndex = (threadIndex + 1) * (pixelBuffer.Length / 4) / _threadNumber * 4;

                if (threadIndex == _threadNumber - 1)
                {
                    endIndex = pixelBuffer.Length;
                }

                GenerateASM(pixelBuffer, startIndex, endIndex, width, centerX, centerY, _vignetteRed, _vignetteGreen, _vignetteBlue, maxDistance, (float)_vignetteIntensity);

            });

            _stopwatch.Stop();

            for (int i = 0; i < pixelBufferByte.Length; i++)
            {
                pixelBufferByte[i] = (byte)pixelBuffer[i];
            }

            _processedImage = new Bitmap(width, height);

            BitmapData resultData = _processedImage.LockBits(new Rectangle(0, 0,
                            width, height),
                            ImageLockMode.WriteOnly, PixelFormat.Format32bppArgb);

            Marshal.Copy(pixelBufferByte, 0, resultData.Scan0, pixelBufferByte.Length);

            _processedImage.UnlockBits(resultData);           
        }

        /**
         * Method generating vignette using a high-level library algorithm
         */
        public void GenerateVignetteCSDLL()
        {
            if (IsImageNull(_loadedImage))
                throw new VignetteException("The image has not been loaded yet.");

            int width = _loadedImage.Width;
            int height = _loadedImage.Height;

            BitmapData sourceData = _loadedImage.LockBits(new Rectangle(0, 0, width, height),
                ImageLockMode.ReadOnly, PixelFormat.Format32bppArgb);

            byte[] pixelBuffer = new byte[sourceData.Stride * sourceData.Height];

            Marshal.Copy(sourceData.Scan0, pixelBuffer, 0, pixelBuffer.Length);

            _loadedImage.UnlockBits(sourceData);

            int centerX = width / 2;
            int centerY = height / 2;

            float maxDistance = (float)(Math.Max(centerX, centerY) * _radiusParam);

            _stopwatch.Reset();
            _stopwatch.Start();

            Parallel.For(0, _threadNumber, threadIndex =>
            {
                int startIndex = threadIndex * (pixelBuffer.Length / 4) / _threadNumber * 4;
                int endIndex = (threadIndex + 1) * (pixelBuffer.Length / 4) / _threadNumber * 4;

                if (threadIndex == _threadNumber - 1)
                {
                    endIndex = pixelBuffer.Length;
                }
                Generator.GenerateVignetteCS(pixelBuffer, startIndex, endIndex, width, centerX, centerY, _vignetteRed, _vignetteGreen, _vignetteBlue, maxDistance, (float)_vignetteIntensity);

            });

            _stopwatch.Stop();

            _processedImage = new Bitmap(width, height);

            BitmapData resultData = _processedImage.LockBits(new Rectangle(0, 0,
                            width, height),
                            ImageLockMode.WriteOnly, PixelFormat.Format32bppArgb);


            Marshal.Copy(pixelBuffer, 0, resultData.Scan0, pixelBuffer.Length);

            _processedImage.UnlockBits(resultData);
        }

        /**
         * Method generating vignette
         */
        public void GenerateVignetteCS()
        {
            if (IsImageNull(_loadedImage))
                throw new VignetteException("The image has not been loaded yet.");

            _stopwatch.Reset();
            _stopwatch.Start();

            int width = _loadedImage.Width;
            int height = _loadedImage.Height;

            BitmapData sourceData = _loadedImage.LockBits(new Rectangle(0, 0, width, height), 
                ImageLockMode.ReadOnly, PixelFormat.Format32bppArgb);

            byte[] pixelBuffer = new byte[sourceData.Stride * sourceData.Height];

            Marshal.Copy(sourceData.Scan0, pixelBuffer, 0, pixelBuffer.Length);

            _loadedImage.UnlockBits(sourceData);

            int centerX = width / 2;
            int centerY = height / 2;

            double maxDistance = Math.Max(centerX, centerY) * _radiusParam;

            Parallel.For(0, _threadNumber, threadIndex => {
                int startIndex = threadIndex * (pixelBuffer.Length / 4) / _threadNumber * 4;
                int endIndex = (threadIndex + 1) * (pixelBuffer.Length / 4) / _threadNumber * 4;

                if (threadIndex == _threadNumber - 1)
                {
                    endIndex = pixelBuffer.Length;
                }

                double red, green, blue, distanceX, distanceY, distance, vignetteFactor;

                int i, j;

                for (int k = startIndex; k + 4 < endIndex; k += 4)
                {

                    i = k / (4 * width);
                    j = (k / 4) % width;

                    distanceX = j - centerX;
                    distanceY = i - centerY;

                    distance = Math.Sqrt((distanceX * distanceX) + (distanceY * distanceY));

                    vignetteFactor = Math.Exp(-distance / maxDistance) * _vignetteIntensity;

                    red = pixelBuffer[k + 2] * vignetteFactor + _vignetteRed;
                    green = pixelBuffer[k + 1] * vignetteFactor + _vignetteGreen;
                    blue = pixelBuffer[k] * vignetteFactor + _vignetteBlue;

                    pixelBuffer[k + 2] = (byte)Math.Min(255, red);
                    pixelBuffer[k + 1] = (byte)Math.Min(255, green);
                    pixelBuffer[k] = (byte)Math.Min(255, blue);

                }
            });

            _processedImage = new Bitmap(width, height);

            BitmapData resultData = _processedImage.LockBits(new Rectangle(0, 0,
                            width, height),
                            ImageLockMode.WriteOnly, PixelFormat.Format32bppArgb);


            Marshal.Copy(pixelBuffer, 0, resultData.Scan0, pixelBuffer.Length);

            _processedImage.UnlockBits(resultData);

            _stopwatch.Stop();

        }
    }
}

