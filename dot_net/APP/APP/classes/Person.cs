using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace APP
{
    /// <summary>
    /// Person object definition for Persons table
    /// </summary>
    public class Person
    {
        public int ID { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string E_Mail { get; set; }
        public string PhoneNumber { get; set; }

        //Navigation ?? Raczej niepotrzebne jesli mamy dwie tabele na pozyczki
        //public virtual ICollection<LoanGranted> LoansGranted { get; set; }
        //public virtual ICollection<LoanObtained> LoansObtained { get; set; }
    }
}
