using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Winietowanie
{
    /**
     * Class representing an exception thrown by VignetteProcessor
     */
    internal class VignetteException : Exception
    {
        public VignetteException() : base() { }

        public VignetteException(string message) : base(message) { }

        public VignetteException(string message, Exception innerException) : base(message, innerException) { }
    }
}
