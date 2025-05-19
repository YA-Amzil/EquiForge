export function calculateAge(birthDate){
    let birthday = new Date(birthDate);
    let now = new Date(Date.now());
    let yearsOld = now.getFullYear() - birthday.getFullYear();
    let monthsOld = now.getMonth() - birthday.getMonth();
    if(monthsOld < 0){
        yearsOld -= 1;
        monthsOld = 12 + monthsOld;
    }
    let daysOld = now.getDate() - birthday.getDate();
    if(daysOld < 0){
        monthsOld -= 1
        daysOld = calculateDaysInMonth(now.getFullYear(), now.getMonth() - 1) + daysOld;
    }
    return yearsOld + ' j ' + monthsOld + ' m ' + daysOld + ' d';
}

export function calculateDaysInMonth(date, month) {
    if (month < 0) {
        month = 11;
    }
    if (month === 1 && date % 4 === 0) {
        return 29;
    }
    if (month === 1) {
        return 28
    }
    if (month % 2 === 0 || month === 7) {
        return 31;
    }
    return 30;
}