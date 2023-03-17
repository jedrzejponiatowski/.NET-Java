using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Remoting.Contexts;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace APP
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            Console.WriteLine("Init commit");

            var context = new PaymentsDB();
            /*var newPerson = new Person() { FirstName = "XXX", LastName = "YYY" };
            context.Persons.Add(newPerson);
            context.SaveChanges();*/

            IList<Person> people;
            people = context.Persons.ToList();

            foreach (var person in people)
            {
                Console.WriteLine(person.ID + " " + person.FirstName + " " + person.LastName);
            }
        }

        private void btnAddCont(object sender, RoutedEventArgs e)
        {
            CFormWindow win2 = new CFormWindow();
            win2.ShowDialog();
        }

        private void TextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }
    }
}
