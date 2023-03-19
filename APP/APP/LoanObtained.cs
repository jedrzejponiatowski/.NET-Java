using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace APP
{
    public class LoanObtained
    {
        public int LoanObtainedID { get; set; }
        public int LoanSize { get; set; }
        public string DateOfIssue { get; set; }
        public string DateOfExpiry { get; set; }


        //Navigation Properties
        public int PersonID { get; set; }
        public Person Person { get; set; }
    }
}
