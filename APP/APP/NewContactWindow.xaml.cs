using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;



namespace APP
{
    /// <summary>
    /// Logika interakcji dla klasy NewContactWindow.xaml
    /// </summary>
    public partial class NewContactWindow : Window
    {
        public NewContactWindow()
        {
            InitializeComponent();
        }

        private void BtnSubmit(object sender, RoutedEventArgs e)
        {
            string firstName = TextBoxFirstName.Text.Trim();
            string lastName = TextBoxLastName.Text.Trim();
            string e_mail = TextBoxEMail.Text.Trim();
            string phone = TextBoxPhoneNumber.Text.Trim();
            Console.WriteLine(firstName);
            Console.WriteLine(lastName);
            Console.WriteLine(e_mail);
            Console.WriteLine(phone);

            Person person = new Person() { FirstName=firstName, LastName=lastName, E_Mail=e_mail, PhoneNumber=phone};
            var context = new PaymentsContext();
            context.AddNewPerson(person);
            
            this.Close();

        }

        private void BtnExit(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
