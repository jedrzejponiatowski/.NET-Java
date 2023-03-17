using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace APP
{
    public class Loan
    {
        public int ID { get; set; }
        public int PersonID { get; set; }
        public virtual Person Person { get; set; }
    }
}
