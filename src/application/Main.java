package application;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;


public class Main extends JFrame implements PropertyChangeListener{

	private static final long serialVersionUID = 1L;
	private float nalogCoefficent = 0.13f; // НДФЛ 13%
	
	private JPanel windowContent = new JPanel();
    
    //создаем шаблон форматирования
    private NumberFormatter nfFloat = new NumberFormatter(NumberFormat.getInstance()); // форматирование float
    private NumberFormatter nfInt = new NumberFormatter(NumberFormat.getIntegerInstance()); // форматирование Int 
    	
	// строка Оклад
	private JLabel casingLable = new JLabel("Оклад, руб");
	private  JFormattedTextField casingTextField = new JFormattedTextField(new DefaultFormatterFactory(nfFloat));
	
	// строка Премия
	private JLabel premiumLable = new JLabel("Премия, руб");
	private JFormattedTextField premiumTextField = new JFormattedTextField(new DefaultFormatterFactory(nfFloat));

	// строка Районный коэффициент
	private JLabel districtCoefficientLable = new JLabel("Районный коэффициент (если есть)");
	private JFormattedTextField districtCoefficientTextField = new JFormattedTextField(new DefaultFormatterFactory(nfFloat));
	
	// строка Количество рабочих дней 
	private JLabel workDaysInMounthLable = new JLabel("Количество рабочих дней в месяце");
	private JFormattedTextField workDaysInMounthTextField = new JFormattedTextField(new DefaultFormatterFactory(nfInt));
	
	// строка Количество отработанных дней
	private JLabel spentDaysInMounthLable = new JLabel("Количество отработанных дней в месяце");
	private JFormattedTextField spentDaysInMounthTextField = new JFormattedTextField(new DefaultFormatterFactory(nfInt));
	
	// кнопка расcчитать
	private JLabel emptyLabel = new JLabel("");
	private JButton button = new JButton("Расcчитать");
	
	// вывод 
	//полная зарплата 
	private JLabel fullwageLable = new JLabel("Полная зарплата, руб");
	private JTextField fullwageTextField = new JTextField("0");

	// НДФЛ
	private JLabel NDFLLable = new JLabel("НДФЛ 13%, руб");
	private JTextField NDFLTextField = new JTextField("0");
		
	//Зарплата на руки
	private JLabel SalaryLable = new JLabel("Зарплата на руки, руб ");
	private JTextField SalaryTextField = new JTextField("0");
	
	//класс для возврата двух элементов 
	public class Pair<T, U> {         
	    public T t;
	    public U u;

	    public Pair(T t, U u) {         
	        this.t= t;
	        this.u= u;
	     }
	 }

	public Main() {
		
		super("Калькулятор расчета заработной платы");
		// Завершить работу программы при закрытии окна
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        //setUpFormats();
		
		// Задаем расположения GridLayout для компонентов панели.
		// Они будут располагаться в таблице размером 8 на 2.
		// Расстояние между ячейками таблицы - 5 пикселей по горизонтали и 5 по вертикали.
		GridLayout gl = new GridLayout(9,2,5,5);
		
		//парамеры окна
		windowContent.setLayout(gl);
		windowContent.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); 
			
		// строка ввода оклада
		windowContent.add(casingLable);		// добавить название поля ввода
		windowContent.add(casingTextField); // добавить поле ввода
		
		// строка ввода премии
		windowContent.add(premiumLable);	
		windowContent.add(premiumTextField);
		
		// строка ввода Районный коэффициент
		windowContent.add(districtCoefficientLable);
		districtCoefficientTextField.setValue(1.0f);
		windowContent.add(districtCoefficientTextField);
		
		// строка ввода Количество рабочих дней
		windowContent.add(workDaysInMounthLable);
		windowContent.add(workDaysInMounthTextField);
		
		// строка ввода Количество отработанных дней
		windowContent.add(spentDaysInMounthLable);
		windowContent.add(spentDaysInMounthTextField);
		
		//кнопка рассчитать
		windowContent.add(emptyLabel);
		
		// действие кнопки
		button.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {

		    	Pair<Boolean,Number> casingPair = CheckField(casingTextField);
		    	Pair<Boolean,Number> premiumPair = CheckField(premiumTextField);
		    	Pair<Boolean,Number> districtCoefficientPair = CheckField(districtCoefficientTextField);
		    	Pair<Boolean,Number> workDaysPair = CheckField(workDaysInMounthTextField);
		    	Pair<Boolean,Number> spentDaysPair = CheckField(spentDaysInMounthTextField);

				
				if(!casingPair.t || !premiumPair.t || !districtCoefficientPair.t || !workDaysPair.t || !spentDaysPair.t) {
		    		//полная зарплата
					fullwageTextField.setText("NaN");
					
					// НДФЛ
					NDFLTextField.setText("NaN");
					
					// зарплата на руки
					SalaryTextField.setText("NaN");
					return;
				}
				
				float casing = casingPair.u.floatValue();
				float premium = premiumPair.u.floatValue();
				
				float districtCoefficient = districtCoefficientPair.u.floatValue();
				int workDays = workDaysPair.u.intValue();
				int spentDays = spentDaysPair.u.intValue();
				
				//полная зарплата
				float fullwage = getFullWage(casing, districtCoefficient, premium, workDays, spentDays);
				if(fullwage>=0)
					fullwageTextField.setText(String.format("%.2f",fullwage)); // заносим результат с округлением до сотых
				else
					fullwageTextField.setText("NaN");
				
				// НДФЛ
				float ndfl = getNDFL(fullwage, nalogCoefficent);
				if(ndfl>=0)
					NDFLTextField.setText(String.format("%.2f",ndfl));
				else
					NDFLTextField.setText("NaN");
				
				// зарплата на руки
				float Salary = getSalary(fullwage, ndfl);
				if(Salary>=0)
					SalaryTextField.setText(String.format("%.2f",Salary));
				else
					SalaryTextField.setText("NaN");
				
		    }
		});
		
		windowContent.add(button);
		
		//вывод
		
		//вывод полной зарплаты
		windowContent.add(fullwageLable);
		windowContent.add(fullwageTextField);
		
		//вывод НДФЛ
		windowContent.add(NDFLLable);
		windowContent.add(NDFLTextField);
		
		//зарплата на руки
		windowContent.add(SalaryLable);
		windowContent.add(SalaryTextField);
		
		setContentPane(windowContent);
		
        setSize(550, 350);
        setVisible(true);
		
	}
	
	/**
     * Процедура проверки и получения числа
     * @param textField - текстовое поле
     * @return результат проверки и число
     */
    private Pair<Boolean, Number> CheckField(JFormattedTextField textField) {
    	
    	Pair<Boolean, Number> pair = new Pair<>(false,-1);
    	if(textField.getValue() == null) return pair;
    	Number number = ((Number) textField.getValue()).floatValue(); // преобразуем для проверки
    	
    	if(number.floatValue()>0) {
    		
    		textField.setBorder(new EtchedBorder(null, Color.gray)); // окрашиваем рамку серым
    		pair.u = number;
    		pair.t = true;
    		
    	}
    	else 
    		textField.setBorder(new LineBorder(Color.red,2)); // окрашиваем рамку красным
    	return pair;
    }
    


	/**
     * Процедура расчета полной зарплаты
     * @param casing - ставка
     * @param сoefficient - коэффициент
     * @param premium - премия
     * @param workDays - рабочие дни
     * @param spentDays - отработанные дни
     * @return возвращает полную зарплату
     */
	public static float getFullWage(float casing, float сoefficient, float premium, int workDays, int spentDays) {
		if(spentDays > workDays || spentDays < 0 || workDays < 0) return -1; //отработанных дней больше рабочих, отработанные и рабочие дни меньше 0
		if(casing <= 0 || сoefficient <= 0) return -1;// если ставка и коифицент меньше 0
		return (float) (casing * сoefficient * (workDays>0 ? ((double) spentDays / workDays) : 0) + premium);
											// если рабочих дней больше 0, то производим деление, иначе возвращаем 0
	}

	/**
     * Процедура расчета НДФЛ
     * @param fullWage - полная зарплата
     * @param nalogCoefficient - налоговая ставка
     * @return возвращает сумму НДФЛ
     */
	public static float getNDFL(float fullWage, float nalogCoefficient) {
		if(fullWage<0 || nalogCoefficient<0) return -1; // проверка на корректность данных
		return fullWage * nalogCoefficient;
	}	
	
	/**
     * Процедура расчета зарплаты на руки
     * @param fullWage - полная зарплата
     * @param ndfl - НДФЛ
     * @return возвращает зарплату на руки
     */
	public static float getSalary(float fullWage, float ndfl) {
		if(fullWage<0 || ndfl<0) return -1; // проверка на корректность данных
		return fullWage - ndfl;
	}


	public static void main(String[] args) {
		new Main();
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		
		
	}
}
