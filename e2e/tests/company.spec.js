import { test, expect } from '@playwright/test';

test('create → list → update → delete company', async ({ page }) => {

  await page.goto('/index.html');

  // CREATE
  await page.fill('#name', 'Playwright Company');
  await page.fill('#budget', '9999');
  await page.click('button[type="submit"]');

  // VERIFY CREATE
  await expect(page.locator('text=Playwright Company')).toBeVisible();

  // UPDATE
    await page.locator('text=Edit').last().click();

    await page.fill('#name', 'Playwright Company Updated');
    await page.click('button[type="submit"]');

    await expect(page.locator('text=Playwright Company Updated')).toBeVisible();

  // DELETE
  await page.locator('text=Delete').last().click();

  await expect(page.locator('text=Playwright Company Updated')).not.toBeVisible();
});


test('validation error when name is empty', async ({ page }) => {

  await page.goto('/index.html');

  await page.fill('#name', '');
  await page.fill('#budget', '1000');
  await page.click('button[type="submit"]');

  await expect(page.locator('#error')).toBeVisible();
});