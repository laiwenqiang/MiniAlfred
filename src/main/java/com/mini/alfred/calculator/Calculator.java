package com.mini.alfred.calculator;

import java.util.Stack;

/**
 * 简单计算器实现，支持加减乘除和小括号
 */
public class Calculator {
    
    /**
     * 计算数学表达式
     * @param expression 数学表达式字符串，如 "1+2*(3+4)"
     * @return 计算结果
     * @throws IllegalArgumentException 如果表达式格式不正确
     */
    public static double calculate(String expression) {
        // 移除所有空格
        expression = expression.replaceAll("\\s+", "");
        
        // 检查表达式是否为空
        if (expression.isEmpty()) {
            throw new IllegalArgumentException("表达式不能为空");
        }
        
        return evaluateExpression(expression);
    }
    
    /**
     * 判断字符串是否是有效的数学表达式
     */
    public static boolean isValidExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }
        
        // 移除所有空格
        expression = expression.replaceAll("\\s+", "");
        
        // 检查是否只包含数字、运算符和括号
        if (!expression.matches("[0-9+\\-*/().]+")) {
            return false;
        }
        
        // 检查括号是否匹配
        int bracketCount = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                bracketCount++;
            } else if (c == ')') {
                bracketCount--;
                if (bracketCount < 0) {
                    return false; // 右括号多于左括号
                }
            }
        }
        
        return bracketCount == 0; // 确保所有括号都匹配
    }
    
    private static double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (Character.isDigit(c) || c == '.') {
                // 解析数字
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                i--; // 回退一位，因为for循环会自增
                numbers.push(Double.parseDouble(sb.toString()));
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                // 计算括号内的表达式
                while (operators.peek() != '(') {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop(); // 弹出左括号
            } else if (isOperator(c)) {
                // 处理运算符优先级
                while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                    if (operators.peek() == '(') {
                        break;
                    }
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }
        
        // 处理剩余的运算符
        while (!operators.isEmpty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }
        
        return numbers.pop();
    }
    
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    
    private static int precedence(char op) {
        if (op == '+' || op == '-') {
            return 1;
        }
        if (op == '*' || op == '/') {
            return 2;
        }
        return 0;
    }
    
    private static double applyOperator(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': 
                if (b == 0) {
                    throw new ArithmeticException("除数不能为零");
                }
                return a / b;
            default: return 0;
        }
    }
}